# 工程简介


访问地址:http://localhost:8787/urule/frame 即可看到urule的规则配置页面

# 延伸阅读

URule Console Quick Start
首先下载到URule Console及URule Core的jar包，如果是Maven项目，则需要在pom.xml中添加如下依赖：
<dependency>
<groupId>com.bstek.urule</groupId>
<artifactId>urule-console</artifactId>
<version>[version]</version>
</dependency>
因为URule Console中架构在Spring之上的，所以需要加载URule Console中提供的Spring配置文件，具体方法是打开web.xml文件，在其中添加Spring的ContextLoaderListener，具体配置如下：
<listener>
<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
<context-param>
<param-name>contextConfigLocation</param-name>
<param-value>/WEB-INF/context.xml</param-value>
</context-param>
上面的context.xml是个位于当前项目WEB-INF目录下的spring配置文件，用于引用URule Console中的Spring配置文件，实现URule Console中Spring配置文件的间接加载，当前context.xml中加载URule Console的Spring配置文件方法就是添加一条import语句，如下所示：
<import resource="classpath:urule-console-context.xml"/>
URule中有一些默认的允许外部覆盖的属性，比如用于指定当前知识库存放目录的urule.repository.dir属性、用于指定URule Console控制台首页显示页面的urule.welcomePage属性等，对于这些属性我们可以新建一个properties文件，在添加设置这些属性值，然后在我们的context.xml文件中通过如下方法加载即可：
<bean parent="urule.props">
<property name="location">
<value>/WEB-INF/configure.properties</value>
</property>
</bean>
这里ID为urule.props的Bean是URule中提供的一个用于加载Spring外部属性文件的Bean，通过上述方式就可以将外部属性文件加载并覆盖URule中默认的属性值，当然如果我们的应用中也有属性需要加载，也可以放在这个文件中一并加载，因为这里通过urule.props加载的spring的属性文件就是标准的spring属性文件加载方式。
最后我们还需要在项目的web.xml当中添加URule Console中的一个Servlet，这个Servlet负责控制台中所有页面与服务端的交互，配置信息如下：
<servlet>
<servlet-name>uruleServlet</servlet-name>
<servlet-class>com.bstek.urule.console.servlet.URuleServlet</servlet-class>
</servlet>
<servlet-mapping>
<servlet-name>uruleServlet</servlet-name>
<url-pattern>/urule/*</url-pattern>
</servlet-mapping>在上面的servlet配置当中，需要注意的是servlet-mapping中的url-pattern的值必须是/urule/*。

运行项目，浏览如下地址，就可以看到URule Console提供的控制台：
http://localhost:[port]/[contextPath]/urule/frame
了解更多信息，请至http://wiki.bsdn.org/display/urule2