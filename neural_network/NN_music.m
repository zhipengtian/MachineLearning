#! /usr/bin/octave -q
arg_list = argv();

%training_file = fopen('music_train.csv');
training_file = fopen(arg_list{1});
tline = fgetl(training_file);
tline = fgetl(training_file);
header = strsplit(tline, ',');
hidden_nodes = 10;
eta = 0.3;      % learning rate

% generate random weights
weights = -1 + 2.*rand(hidden_nodes, numel(header) - 1);
hidden_weights = -1 + 2.*rand(1, hidden_nodes);

% load data
i = 0;
while ischar(tline)
    i = i + 1;
    row = strsplit(tline, ',');
    if (strcmpi(row(numel(row)), 'yes'))
        target(i) = 1.0;
    elseif (strcmpi(row(numel(row)), 'no'))
        target(i) = -1.0;
    else
        target(i) = str2double(row(numel(row)));
    end
    
    for j = 1 : numel(row)-1
        if (strcmpi(row(j), 'yes'))
            data(i, j) = 1.0;
        elseif (strcmpi(row(j), 'no'))
            data(i, j) = -1.0;
        else
            if j == 1
                data(i, j) = ((2000 - str2double(row(j))) / 50) - 1;
            elseif j == 2
                data(i, j) = ((7 - str2double(row(j))) / 3.5) - 1;
            else
                data(i, j) = str2double(row(j));
            end
        end
    end
    
    tline = fgetl(training_file);
end
fclose(training_file);

data_size = size(data);

% training
round = 0;
while true
    round = round + 1;
    error = 0;
    for i = 1 : data_size(1)
        
        hidden_outputs = zeros(1, hidden_nodes);
    
        for n = 1 : hidden_nodes
            hidden_outputs(n) = 1 / (1 + exp(-sum(data(i, :) .* weights(n, :))));
        end
    
        output = 1 / (1 + exp(-sum(hidden_weights .* hidden_outputs)));
        error = error + (target(i) - output) ^ 2;
        
        % hidden - output weights adjustment
        output_error = output * (1 - output) * (target(i) - output);
        delta_hidden_out = hidden_outputs * output_error * eta;
        hidden_weights = hidden_weights + delta_hidden_out;
    
        % input - hidden weights adjustment
        delta_in_hidden = output_error * hidden_weights .* hidden_outputs .* (1 - hidden_outputs);
        for h = 1 : hidden_nodes
            weights(h, :) = weights(h, :) + delta_in_hidden(h) .* data(i, :) * eta;
        end
    end
    
    %error = error / data_size(1);
    
    if (abs(error) < 60)
        break;
    end
    
    if mod(round, 10) == 0
        fprintf('%.1f\n', error);
    end
    
end

fprintf('TRAINING COMPLETED! NOW PREDICTING.\n');

% predict result

%test_file = fopen('music_dev.csv');
test_file = fopen(arg_list{2});
tline = fgetl(test_file);
tline = fgetl(test_file);
while ischar(tline)
    row = strsplit(tline, ',');
    input = zeros(1, numel(row));
    for r = 1 : numel(row)
        if (strcmpi(row(j), 'yes'))
            input(r) = 1.0;
        elseif (strcmpi(row(j), 'no'))
            input(r) = -1.0;
        else
            if r == 1
                input(r) = ((2000 - str2double(row(r))) / 50) - 1;
            elseif r == 2
                input(r) = ((7 - str2double(row(r))) / 3.5) - 1;
            else
                input(r) = str2double(row(r));
            end
        end
    end
    
    hidden_outputs = zeros(1, hidden_nodes);   
    for n = 1 : hidden_nodes
        hidden_outputs(n) = 1 / (1 + exp(-sum(input .* weights(n, :))));
    end
    
    output = 1 / (1 + exp(-sum(hidden_weights .* hidden_outputs)));
    if output >= 0.5
        result = 'yes';
    else
        result = 'no';
    end
    
    fprintf('%s\n', result);

    tline = fgetl(test_file);
end
fclose(test_file);


