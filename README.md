# QRPC: A Custom RPC Framework

QRPC is a custom, lightweight RPC (Remote Procedure Call) framework built for high performance and extensibility. This project follows modern best practices in distributed systems, leveraging advanced features like SPI mechanism, factory patterns, and a variety of serialization, registration, and load-balancing strategies.

## Features

- **Global Configuration**
    - Dynamic global configuration management for enhanced flexibility.

- **Serialization**
    - Support for multiple serialization mechanisms (JSON, Protobuf, etc.).
    - Extensible through SPI + Factory patterns.

- **Registration Center**
    - Pluggable registration center implementations (Etcd, ZooKeeper).
    - Optimizations for heartbeats, service caching, and auto-expiry.

- **Custom Protocol**
    - TCP-based transport layer designed using Vert.x for high concurrency.
    - Implements custom messaging structure inspired by Dubbo.
    - Solutions for packet fragmentation and reassembly.

- **Load Balancing**
    - Support for a variety of load balancing algorithms including Consistent Hashing.
    - Easily extensible with custom load balancers using SPI + Factory.

- **Retry and Fault Tolerance Mechanisms**
    - Multiple retry strategies with customizable backoff policies.
    - Pluggable fault-tolerance strategies for robust error handling.

- **Pluggable Architecture**
    - SPI (Service Provider Interface) used across the framework to allow for customization and extension of components.
    - Factory pattern for creating extensible, scalable services.

- **Quick Startup**
    - Designed with annotation-driven configurations for easy setup using Spring Boot Starter.

## Installation

To get started with QRPC, clone this repository and include it in your project:

```bash
git clone https://github.com/CodeinMac/qrpc.git
```

## Maven/Gradle Integration

Add the following to your `pom.xml` or `build.gradle`:

### Maven

```xml
<dependency>
    <groupId>com.yourorganization</groupId>
    <artifactId>qrpc</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'com.yourorganization:qrpc:1.0.0'
```
## Usage

Here’s how to initialize and configure QRPC in your project:

1. **Configuration**: Define global configurations for the framework.
2. **Register Services**: Use the Etcd or ZooKeeper registration center for service registration.
3. **Serialization**: Select and configure the serialization mechanism (e.g., JSON, Protobuf).
4. **Start the Framework**: Launch the RPC services with the QRPC quick-start class.

### Example

```java
QRpcServer server = new QRpcServer();
server.registerService(MyService.class, new MyServiceImpl());
server.start();
```

## Extending QRPC

The QRPC framework is designed for easy extensibility. Using the SPI and Factory patterns, you can plug in your own implementations for various components such as:

- Serialization strategies
- Registration centers
- Load balancers
- Retry mechanisms
- Fault-tolerance strategies

## Contributing

We welcome contributions! Please fork the repository and submit a pull request to propose changes or add new features.

## License
This project is licensed under the MIT License. See the LICENSE file for details.
