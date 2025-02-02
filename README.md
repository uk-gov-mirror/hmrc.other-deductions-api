Other Deductions API
========================
This API allows software packages to retrieve, create and amend, and delete deductions that have been previously populated.

## Requirements
- Scala 2.12.x
- Java 8
- sbt 1.3.7
- [Service Manager](https://github.com/hmrc/service-manager)
 
## Development Setup
 
Run from the console using: `sbt run` (starts on port 7797 by default)
 
Start the service manager profile: `sm --start MTDFB_ALL`
 
## Running tests
```
sbt test
sbt it:test
```

## Viewing RAML

To view documentation locally ensure the Deductions API is running, and run api-documentation-frontend:
`./run_local_with_dependencies.sh`

Then go to http://localhost:9680/api-documentation/docs/api/preview and use this port and version:
`http://localhost:7797/api/conf/1.0/application.raml`

## Reporting Issues

You can create a GitHub issue [here](https://github.com/hmrc/other-deductions-api/issues)

## API Reference / Documentation 
Available on the [Other Deductions Documentation](https://developer.service.hmrc.gov.uk/api-documentation/docs/api/service/other-deductions-api/1.0)

## License

This code is open source software licensed under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html)
