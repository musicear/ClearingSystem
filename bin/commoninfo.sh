BATCH_HOME=..
_LIBJARS=.:$BATCH_HOME/lib:$BATCH_HOME/lib/mq
for i in `ls $BATCH_HOME/lib/*.jar` 
do 
	_LIBJARS=$_LIBJARS:$i
done
for i in `ls $BATCH_HOME/lib/mq/*.jar` 
do 
	_LIBJARS=$_LIBJARS:$i
done
_LIBJARS=../classes/:$_LIBJARS
BATCH_CP=$_LIBJARS
echo $BATCH_CP
java -Xmx600M -cp $BATCH_CP junit.TestSendToMQ
