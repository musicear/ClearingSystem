MQ=/opt/mqm
ORACLE=/oracle/product/12.2.0.1
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$MQ/java/lib64
export CP_LIB=../classes:../lib/*:$MQ/java/lib/*:$ORACLE/jdbc/lib/*
echo $CP_LIB
