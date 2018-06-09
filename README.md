# A Java library for user data and program data

- [Help us improve](#help_us_improve)
- [Overview](#overview)
  - [Requirements](#requirements)
- [Maven install](#maven_install)
- [License](#license)

## <a name='help_us_improve'>Help us improve</a>

This Data library was developed by Siloft (https://siloft.com/). We hope users of this library are willing to help us maintain, improve, and share this library to satisfy all the needs of people around the world.

## <a name='overview'>Overview</a>

The Siloft Data library is an easy-to-use Java library which handles reading and writing user data and program data files. The following file paths will be used depending on the system, assuming the organisation is "Siloft", program is "TestProgram", and file is "Options":

- User data
  - Windows: %LOCALAPPDATA%\Siloft\TestProgram\Options
  - Linux: ~/.Siloft/TestProgram/Options
  - Mac OS: ~/Library/Application Support/Siloft\TestProgram\Options
- Program data
  - Windows: %PROGRAMDATA%\Siloft\TestProgram\Options
  - Linux: /etc/Siloft/TestProgram/Options
  - Mac OS: /Library/Application Support/Siloft\TestProgram\Options

### <a name='requirements'>Requirements</a>

The following requirements are attached to this library:
  - Library works only on Windows, Linux, and Mac OS systems
  - Java 8 (or higher), standard (SE)

## <a name='maven_install'>Maven install</a>

The Siloft Data library for Java is easy to install, and you can download the binary directly from the [Downloads page](https://siloft.com/), or you can use Maven.
To use Maven, add the following lines to your pom.xml file:

```maven
<project>
  <dependencies>
    <dependency>
      <groupId>com.siloft</groupId>
      <artifactId>siloft-data</artifactId>
      <version>0.8.0</version>
    </dependency>
  </dependencies>
</project>
```

## <a name='license'>License</a>

[Siloft Data library](https://siloft.com/) is open-source and licensed under the [MIT License](./LICENSE.md).
