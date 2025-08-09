FROM openjdk:17

ADD target/Wallet-1.0.jar /wallet/

WORKDIR /wallet/

EXPOSE 8080 8080

CMD ["java", "-jar", "Wallet-1.0.jar"]