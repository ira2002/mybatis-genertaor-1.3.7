# mybatis-genertaor-1.3.7
Mybatis逆向工程生成代码
<br>
<b>使用说明:</b>
<br>
1、config----_Ibator_Run_Test_v1_3_7.java； 修改main函数中加载的配置文件， 修下面的run方法中的myBatisGenerator.generate（null）部分，
<br>
2、文件配置参考config/myConfig.xml；注意：配置中生成的代码存放位置要填绝对路径。建议另起一个配置文件。
<br>
3、数据库连接参数和项目生成目录参数已通过properties配置在jdbc。properties文件中。
<br>
4、更多配置信息见config/Mybatis Generator最完整配置详解.xml(版本不同时可能其对应的属性有出入，具体需要进入到源代码所在类中查看)
<br>
