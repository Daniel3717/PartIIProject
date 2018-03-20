lXsCol = 4;
lYsCol = 9;
lBasePath = "..\PartIIProject\Statistics\Memory\DataQueens";
lFile = lBasePath+1+1+1+".txt";
complexPlot(lXsCol,lYsCol,lFile);
hold on
lFile = lBasePath+1+1+2+".txt";
%complexPlot(lXsCol,lYsCol,lFile);
hold off
legend("111","112");

function complexPlot(pXsCol, pYsCol, pFile)
    lData = importdata(pFile)
    lXs = lData(:,pXsCol);
    lYs = log10(lData(:,pYsCol));
    lMap = containers.Map('KeyType','double','ValueType','any');
    for lIndex = 1:size(lXs)
        lCurrentX = lXs(lIndex);
        lCurrentY = lYs(lIndex);
        if (~isKey(lMap,lCurrentX))
            lMap(lCurrentX) = (lCurrentY);
        else
            lArray = lMap(lCurrentX);
            lArray = [lArray,lCurrentY];
            lMap(lCurrentX) = lArray;
        end
    end
    
    lMapKeys = keys(lMap);
    lUniqueXs = double(zeros(size(lMapKeys)));
    for lIndex = 1:size(lMapKeys,2)
        lMapKey = lMapKeys(1,lIndex);
        lUniqueXs(lIndex)=lMapKey{1};
    end
    
    lMeanYs = double(zeros(size(lUniqueXs)));
    lCI = double(zeros(size(lUniqueXs)));
    lCIMin = double(zeros(size(lUniqueXs)));
    lCIMax = double(zeros(size(lUniqueXs)));
    for lIndex = 1:size(lUniqueXs,2)
        lCurrentX = lUniqueXs(1,lIndex);
        lCurrentYs = lMap(lCurrentX);
        lMean = mean(lCurrentYs);
        lMeanYs(lIndex) = lMean;
        
        % Adapted from https://uk.mathworks.com/matlabcentral/answers/159417-how-to-calculate-the-confidence-interval
        SEM = std(lCurrentYs)/sqrt(length(lCurrentYs));                 % Standard Error
        ts = tinv([0.025  0.975],length(lCurrentYs)-1);                 % T-Score
        CI = ts*SEM;                                                    % Confidence Intervals
        
        lCIMin(lIndex) = CI(1);
        lCIMax(lIndex) = CI(2);
    end

    errorbar(lUniqueXs,lMeanYs,lCIMin,lCIMax);
    
end