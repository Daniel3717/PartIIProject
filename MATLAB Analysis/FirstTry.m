names={};
hold on
for lVO = 0:2
    for lVS = 0:4
        for lLB = 0:2
            value = lVO*100+lVS*10+lLB;
            %if ((value==0)||(value==111)||(value==122)||(value==231)||(value==242))
                Path = "..\PartIIProject\Statistics\Data"+lVO+lVS+lLB+".txt";
                Data = importdata(Path);
                X = Data(:,4);
                Y = Data(:,7);
                plot(X,Y)
                names{end+1} = ""+lVO+lVS+lLB;
            %end
        end
    end
end
legend(names);
hold off

            
%{
Data110 = importdata("..\PartIIProject\Statistics\Data110.txt");
Data111 = importdata("..\PartIIProject\Statistics\Data111.txt");
Data012 = importdata("..\PartIIProject\Statistics\Data012.txt");
Data112 = importdata("..\PartIIProject\Statistics\Data112.txt");
Data212 = importdata("..\PartIIProject\Statistics\Data212.txt");

X110 = Data110(:,4);
Y110 = Data110(:,7);

X111 = Data111(:,4);
Y111 = Data111(:,7);

X012 = Data112(:,4);
Y012 = Data112(:,7);

X112 = Data112(:,4);
Y112 = Data112(:,7);

X212 = Data112(:,4);
Y212 = Data112(:,7);

plot(X110,Y110)
hold on
plot(X111,Y111)
plot(X112,Y112)
plot(X012,Y012)
plot(X212,Y212)
hold off
legend('110','111','112','012','212')
%}