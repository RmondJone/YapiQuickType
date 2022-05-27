# 1. Business Background

In daily development, we will use the tool YApi. YApi is a local deployable, front-end and back-end and QA, visual interface management platform, which can perform some operations of interface definition and interface simulation.

For more YApi usage tutorials, you can refer to the [YApi official website](https://github.com/YMFE/yapi), and I won't go into details here.

This plug-in is a coding tool plug-in based on the YApi open source interface. It is mainly used for the rapid generation of the interface definition entity code. As long as a simple configuration is performed in the plug-in settings options, you can quickly generate a variety of items you want with one click. The interface defines the entity code.

# 2. Plug-in download and configuration

### Download

You can directly go to the open source address of the plug-in to download the Release version, and after downloading it locally, the IDE can import it locally. Plug-in open source address: [https://github.com/RmondJone/YapiQuickType](https://github.com/RmondJone/YapiQuickType)

You can also search for Yapi QuickType directly in the plugin market to download

![](http://www.guohanlin.com/images/quicktype_anzhuang.png)

### Plugin Configuration

![](http://www.guohanlin.com/images/quicktype_setting.png)

* **Fill in the self-built address for the YApi root path**
* **Configure the Id and Token of the project you need**
  
  These can be found in the YApi project configuration, find and copy and fill in, and the project name can be filled in at will.
  
![](http://www.guohanlin.com/images/yapi_id.jpg)

![](http://www.guohanlin.com/images/yapi_token.jpg)

# 3.Use of plugins and effects

##### （1）The use of the plugin is also very simple, just right-click on the directory where you want to generate the code

![](https://plugins.jetbrains.com/files/18847/screenshot_d83ff2d1-1ffa-4b68-a7c0-40487fa583c4)

##### （2）YApi code generation plugin: select the corresponding interface of the project you configured and the language you want to generate

![](https://plugins.jetbrains.com/files/18847/screenshot_66f98b03-5374-4e4e-ba38-742656c057ee)

##### （3）JSON code generation plugin: paste the copied JSON string, enter the generated entity name, select the language you want to generate, and click OK to generate the code

![](https://plugins.jetbrains.com/files/18847/screenshot_e8ff159d-deaa-4354-a526-5874c80fd76b)

##### （4）Generate code effects

![](https://plugins.jetbrains.com/files/18847/screenshot_db0c3a01-6dda-480d-8ede-27bb11dc0d30)

**Note: IDE build numbers lower than 203 use FIX-IU171 version**

# 4.Precautions
If the request to QuickTypeNode service fails, you can try to configure the following host

```
104.168.153.3 quicktype.guohanlin.com
```

If the configuration of Host still does not work, you may need to connect to the external network to request the Node service.

# 5.Use feedback

Welcome to my telegram group:[https://t.me/YApiQuickType](https://t.me/YApiQuickType)

<img src="http://www.guohanlin.com/images/quicktype_tg_group.jpg" width="300"/>

[中文文档](./README_CN.md)