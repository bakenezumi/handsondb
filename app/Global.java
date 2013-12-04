import java.lang.reflect.Method;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.atteo.evo.classindex.ClassIndex;
import org.mybatis.guice.MyBatisModule;

import play.Application;
import play.GlobalSettings;
import play.db.DB;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Http.HeaderNames;
import play.mvc.Http.Request;
import play.mvc.SimpleResult;
import utils.mybatis.MybatisMapper;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.name.Names;

public class Global extends GlobalSettings {

	private Injector injector;

	@Override
	public void onStart(Application app) {
		injector = Guice.createInjector(new MyBatisModule() {
			@Override
			protected void initialize() {
				bindDataSourceProvider(new Provider<DataSource>() {
					@Override
					public DataSource get() {
						// use db as configured in conf/application.conf
						return DB.getDataSource();
					}
				});
				bindTransactionFactoryType(JdbcTransactionFactory.class);

				final Properties myBatisProperties = new Properties();
				myBatisProperties.setProperty("mybatis.environment.id", "default");
				Names.bindProperties(binder(), myBatisProperties);
				for (Class<?> clazz : ClassIndex.getAnnotated(MybatisMapper.class)) {
					addMapperClass(clazz);
				}

			}
		});
		Configuration conf = injector.getInstance(Configuration.class);
		conf.setCallSettersOnNulls(true);
	}

	@Override
	public <A> A getControllerInstance(Class<A> clazz) throws Exception {
		return injector.getInstance(clazz);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Action onRequest(Request request, Method actionMethod) {
		return new Action.Simple() {
			@Override
			public F.Promise<SimpleResult> call(Context ctx) throws Throwable {
				try {
					//no-cache
					ctx.response().setHeader(HeaderNames.CACHE_CONTROL, "no-cache");
					F.Promise<SimpleResult> ret = delegate.call(ctx);
					return ret;
				} catch (Exception e) {
					e.printStackTrace();
					return F.Promise.pure((SimpleResult) internalServerError(e.getMessage()));
				}
			}
		};
	}

}