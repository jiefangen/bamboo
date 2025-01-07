# Web开发代码规约

Web应用开发分为两种模式：

- 前后端分离模式，开发时通过import引入依赖库，通过webpack这样的构建技术生成最终在浏览器中运行的代码结构。
- 服务端渲染模式，开发时通过requirejs这样的UMD技术引入依赖库，无需构建，可以直接在浏览器中运行。

前后端分离模式在绝大部分场景下适用，是默认的开发模式，但不利于SEO，对于需要进行SEO的页面，以服务端渲染模式实现，作为补充。 然而是否需要SEO是一个动态的需求，开发人员并不能很好的界定，为了简化和明确规则，我们规定：
所有登录后才能访问的页面均使用前后端分离模式，所有匿名即可访问的页面均使用服务端渲染模式。

本框架提供了这两种模式的页面可同时存在的实现方式。

## 一、请求

前端通过http/https协议向后端发送请求，以阻塞的、同步的mvc方式处理，而不是非阻塞的、异步的reactive方式处理。

无论是前后端分离模式中的RPC请求，还是服务端渲染模式中的页面跳转请求，在Controller中都对应一个方法，区别只是返回结果一个是数据对象，一个是视图， 我们将两者均视为Api，遵守以下Api规约。

1. 【强制】一个http请求由请求方法、url、请求参数、响应结果组成，请求头信息由浏览器或AJAX框架设定，请求者无需也不应该关心，Api也不能设计成与请求头信息相关。
2. 【强制】查询类请求使用GET请求方法，命令类请求使用POST请求方法，不使用其它如PUT、DELETE请求方法。

> 命令类请求：在业务语义上会变更数据的请求，即使也可能返回数据，如：新增、修改、删除。  
> 查询类请求：在业务语义上仅仅是获取/呈现数据，不会变更数据的请求，即使后端处理可能进行数据存储，如：操作日志。

3. 【强制】url的命名遵守[《JavaEE代码规约》](java.md)的通用规约中命名的所有规约，在此基础上还遵守以下规约：

- url按模块、功能点、资源、操作等划分成多个层级，每个层级以/开头进行分隔，同一个层级中多个单词之间用-分割，均使用小写字母，没有后缀名。
- url的多个层级与对应Controller类的相对包名、类名、方法名存在对应关系，用下表举例说明这种对应关系， 其中Controller的类名为 [微服务顶级包名]
  .web.controller.user.MemberRoleController，具有注解@RequestMapping("/user/member-role")。

  url|请求方法|Controller方法|说明
        ---|---|---|---
  /user/member-role/list|GET|list()|查询成员角色清单
  /user/member-role/add|POST|add()|新增成员角色
  /user/member-role/{id}|GET|detail()|获取指定id的成员角色详情
  /user/member-role/{id}/update|POST|update()|修改指定id的成员角色
  /user/member-role/{id}/delete|POST|delete()|删除指定id的成员角色
  /user/member-role/{id}/disable|POST|disable()|禁用指定id的成员角色

4. 【强制】请求参数的命名遵守[《JavaEE代码规约》](java.md)的Java属性/参数/局部变量的命名规约。
5. 【强制】GET请求的请求参数通过parameter形式传递，不允许通过body形式传递，在Controller方法中通过@RequestParam注解接收参数。
6. 【强制】POST请求的请求参数优先通过body形式传递，在Controller方法中通过@RequestBody注解的CommandModel类接收参数。
   参数个数≤3个的，允许通过parameter形式传递，这是为了少写一些的简单CommandModel类，提高开发效率。
   也允许这两种形式同时使用，不过parameter形式传递的参数个数同样必须≤3个，这是为了可以复用CommandModel类，同样是为了提高开发效率。
7. 【强制】包含parameter形式请求参数的url，总长度不能超过2048位。

> 说明：不同浏览器对于url的最大长度限制略有不同，并且对超出最大长度的处理逻辑也有差异，2048字节是取所有浏览器的最小值。
> 2048位已经足够适应几乎所有的业务场景，如果GET请求的请求参数长度超出限制，就需要重新设计api，甚至重新设计页面和需求。

8. 【强制】RPC请求的响应结果以JSON的格式传递，对于集合类型的结果（数组/Collection），至少返回空集合[]，而不返回null，以减少前端的为null判断。

## 二、代码

1. 【强制】所有代码文件和资源文件都采用小写字母，单词之间用-分隔，且所属目录与对应的url吻合。

> 例外：Vue的自定义通用组件*.vue文件，采用驼峰法，UpperCamelCase风格，首字母大写，不能包含下划线。

2. 【强制】对外公开的html文件扩展名使用*.html，内部作为模板范例的html文件扩展名使用*.htm。
3. 【强制】html标签的属性值使用双引号含括，而不使用单引号。
4. 【强制】css样式名称一律小写，单词之间用-分隔。
5. 【推荐】应用工程特有的css样式名称以my-开头，以便于与框架样式区分。
6. 【强制】JavaScript代码中的字符串使用单引号含括，而不使用双引号；必须使用 === 和 !== 进行相等和不等判断，而不使用 == 和 != 。
6. 【推荐】套用JavaDoc的格式对JavaScript的类、属性、函数进行注释，其它注释规范与Java相同。
7.

【强制】使用idea编辑html、jsp和js文件，而不用eclipse进行编辑。idea对这些代码的格式化比eclipse好很多，格式化配置从[idea-code-style.xml](ide/idea-code-style.xml)
导入。

## 三、前后端分离模式

1. 【强制】不论前端使用什么技术框架，均应遵守该技术框架的标准规范。
2. 【推荐】在package.json中eslintConfig.rules节点添加如下设定：

    ```(json)
   "rules": {
     "no-unused-vars": "warn", // 未使用的变量进行警告
     "eqeqeq": "warn" // 对未使用===进行相等判断进行警告
   }
   ```

3. 【强制】对于vue-cli工程，webpack构建时不压缩html代码，通过在vue.config.js中加入如下配置实现：

    ```(js)
    pages: {
        index: {
            entry: 'src/main.js',
            template: 'public/index.html',
            filename: 'index.html',
            minify: false, // 不压缩html代码
        }
    }
    ```
   > 说明：压缩的html代码不便于sitemesh装饰。

4. 【强制】JavaScript代码格式设置，在工程根目录下添加.editorconfig文件，内容如下：

    ```
   [*]
   charset = utf-8
   end_of_line = lf
   insert_final_newline = true
   indent_style = space
   indent_size = 4
   
   [*.json]
   indent_size = 2
   ```

## 四、服务端渲染模式

1. 【推荐】服务端渲染技术采用jsp。  
   在已有服务端Java运行环境的情况下，再搭建服务端nodejs运行环境，增加了系统复杂度，并不可取，因此服务端渲染不采用nodejs技术。  
   在Java技术中，jsp和 freemarker / velocity / thymeleaf 这样的Java模板引擎技术都有各自的优缺点，采用服务端渲染模式开发的页面很少，
   模板引擎还需要额外的学习成本，因此我们选择jsp，并制定以下规约，以最大程度地避免jsp的弊端。
2. 【强制】jsp页面必须以<%@ page language="java" contentType="text/html; charset=utf-8" %>作为第一行开头。
3. 【强制】禁止在jsp页面中使用<% %>、<%! %>、<%= %>标签，只能使用<%@ %>、jstl标签和表达式，以及自定义标签。
4. 【强制】jsp页面中的html代码必须严格遵守html的规范。
    