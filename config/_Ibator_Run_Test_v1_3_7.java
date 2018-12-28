
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.apache.log4j.Logger;

/**
 * mybatis-generator.1.3.7 启动入口
 * 	（此高版本不支持targetRuntime=Ibatis2Java5生成ibatis）
 * @author	chdaba
 * @version	1.0.0
 * @date	2018年12月26日 下午2:55:09
 */
public class _Ibator_Run_Test_v1_3_7 {
	private static final Logger logger = Logger.getLogger(_Ibator_Run_Test_v1_3_7.class);

	public static void main(String[] args) {
		_Ibator_Run_Test_v1_3_7 test = new _Ibator_Run_Test_v1_3_7();
		test.run("myConfig.xml");
	}

	public void run(String fileName) {
		try {
			List<String> warnings = new ArrayList<String>();
			ConfigurationParser cp = new ConfigurationParser(warnings);
			Configuration config = cp.parseConfiguration(this.getClass().getClassLoader().getResourceAsStream(fileName));
			logger.debug(this.getClass().getClassLoader().toString());
			DefaultShellCallback shellCallback = new DefaultShellCallback(true);
			MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
			//myBatisGenerator.generate(null); //默认会执行contexts下的所有context配置
			//以下为根据contextIds选择执行
			Set<String> contextIds = new HashSet<String>();
			contextIds.add("springBootDemo");
			myBatisGenerator.generate(null, contextIds);
			
			logger.debug("成功");
		} catch (Exception e) {
			logger.error("Exception:", e);
		}
	}

}
