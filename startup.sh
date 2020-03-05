#JAVA_OPTS="$JAVA_OPTS -Xms1G -Xmx4G -XX:+UseG1GC -Djava.library.path=/usr/local/lib"
JAVA_OPTS="$JAVA_OPTS -Xms1G -Xmx4G -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC"
export JAVA_OPTS

java $JAVA_OPTS -jar jzmq-producer.jar &