### UEditor SpringMVC 版
     和jsp版区别主要是配置初始化部分和文件上传部分做了修改,主要目的是能在SpringMVC项目中灵活使用.
     1.初始化时修改为可以设定配置文件目录,去掉原来的servlet-jsp式获取配置文件的方式,以适用于在SpringMVC环境中,请求Controller来获取配置信息.
     2.修改上传文件部分功能的实现,现在使用CommonsMultipartResolver来实现.
#### 注意:除了 json.jar 包外,其它包使用SpringMVC项目环境中的包,没有再附加到UEditor-1.4.3.jar包中. 