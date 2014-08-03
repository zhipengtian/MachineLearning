#! /usr/bin/octave -q
arg_list = argv();

training_file = fopen(arg_list{1});
%training_file = fopen('education_train.csv');
tline = fgetl(training_file);
tline = fgetl(training_file);
header = strsplit(tline, ',');
hidden_nodes = 10;
eta = 1;      % learning rate

% generate random weights
weights = -1 + 2.*rand(hidden_nodes, numel(header) - 1);
hidden_weights = -1 + 2.*rand(1, hidden_nodes);

% load data
i = 0;
while ischar(tline)
    i = i + 1;
    row = strsplit(tline, ',');
    target(i) = str2double(row(numel(row))) / 100;
    
    for j = 1 : numel(row)-1
        if (strcmpi(row(j), 'yes'))
            data(i, j) = 1.0;
        elseif (strcmpi(row(j), 'no'))
            data(i, j) = 0.0;
        else
            data(i, j) = ((100 - str2double(row(j))) / 50) - 1;
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
        error = error + (target(i) * 100 - output * 100) ^ 2;
        
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
    
    if (abs(error) < 250)
        break;
    end
    
    if mod(round, 10) == 0
        fprintf('%.1f\n', error);
    end
    
end

fprintf('TRAINING COMPLETED! NOW PREDICTING.\n');

% predict result

test_file = fopen(arg_list{2});
%test_file = fopen('education_dev.csv');
tline = fgetl(test_file);
tline = fgetl(test_file);
while ischar(tline)
    row = strsplit(tline, ',');
    input = zeros(1, numel(row));
    for r = 1 : numel(row)
        input(r) = ((100 - str2double(row(r))) / 50) - 1;
    end
    
    hidden_outputs = zeros(1, hidden_nodes);   
    for n = 1 : hidden_nodes
        hidden_outputs(n) = 1 / (1 + exp(-sum(input .* weights(n, :))));
    end
    
    output = 1 / (1 + exp(-sum(hidden_weights .* hidden_outputs)));
    score = output * 100;
    fprintf('%.1f\n', score);

    tline = fgetl(test_file);
end
fclose(test_file);


