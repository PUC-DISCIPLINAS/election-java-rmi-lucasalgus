run-client:
	cp -r rmi.policy ./bin
	cd bin && java -Djava.security.policy=rmi.policy -Djava.rmi.server.useCodebaseOnly=false -Djava.rmi.server.hostname=localhost -Djava.rmi.server.codebase=file:Users/lucas/dev/University/election-java-rmi-lucasalgus/bin Client

run-server:
	cp -r rmi.policy ./bin
	cd bin && java -Djava.security.policy=rmi.policy -Djava.rmi.server.useCodebaseOnly=false -Djava.rmi.server.hostname=localhost -Djava.rmi.server.codebase=file:Users/lucas/dev/University/election-java-rmi-lucasalgus/bin Main

compile:
	rm -rf bin
	mkdir bin
	cd src && javac *.java && mv -f *.class ../bin