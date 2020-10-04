. ./javaenv.sh
YYMM=`date +%Y%m%d`
java -verbose:gc -cp $CP_LIB resoft.basLink.BasLinkServer 2>../classes/tmp/tips$YYMM.log
