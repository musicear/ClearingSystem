jarlist=`ls ../lib/*.jar |grep jar`
for jarname in $jarlist
do
echo "检索 $jarname"
jar -tvf $jarname|grep class|grep $1
done
