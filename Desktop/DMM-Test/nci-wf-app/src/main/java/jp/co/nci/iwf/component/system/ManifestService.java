package jp.co.nci.iwf.component.system;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.servlet.ServletContext;

import jp.co.nci.iwf.servlet.listener.NciWfAppInitializer;
import jp.co.nci.iwf.util.MiscUtils;
/**
 * META-INF/MANIFEST.MFの情報を保持するクラス
 * @author nakamura.mitsuyuki
 *
 */
@ApplicationScoped
public class ManifestService extends MiscUtils {
	/** MANIFEST.MFの格納パス */
	private static final String MANIFEST_PATH = "/META-INF/MANIFEST.MF";
	/** ServletContext.setAttribute()するときのキー */
	public static final String KEY = "MANIFEST";


	/** pom.xml の project.name */
	private String name = "nci-wf-app";
	/** pom.xml の project.groupId */
	private String groupId;
	/** pom.xml の project.artifactId */
	private String artifactId;
	/** pom.xml の project.version */
	private String version = "NIGHTY-BUILD";
	/** war/jarを作成したユーザ */
	private String buildBy;
	/** war/jarを作成したJDK */
	private String buildJdk;
	/** war/jarを作成した時刻 */
	private String buildTimestamp;

	@PostConstruct
	public void init() {
		ServletContext ctx = NciWfAppInitializer.get();
		try (InputStream in = ctx.getResourceAsStream(MANIFEST_PATH)){
			// META-INF/MANIFEST.MFはMaven Pluginによって生成されるので、デバッグ実行時には存在しない
			if (in != null) {
				final Manifest manifest = new Manifest(in);
				final Attributes attr = manifest.getMainAttributes();

				this.groupId = defaults(attr.getValue("Project-GroupId"), "");
				this.artifactId = defaults(attr.getValue("Project-ArtifactId"), "");
				this.version = defaults(attr.getValue("Project-Version"), "NIGHTY-BUILD");
				this.name = defaults(attr.getValue("Project-Name"), "nci-wf-app");
				this.buildBy = defaults(attr.getValue("Built-By"), "");
				this.buildJdk = defaults(attr.getValue("Build-Jdk"), "");
				this.buildTimestamp = defaults(attr.getValue("Build-Timestamp"), "");
			}
		}
		catch (IOException e) {
			throw new RuntimeException(MANIFEST_PATH + "の読み込みに失敗しました", e);
		}
	}

	/** pom.xml の project.name */
	public String getName() {
		return name;
	}

	/** pom.xml の project.groupId */
	public String getGroupId() {
		return groupId;
	}

	/** pom.xml の project.artifactId */
	public String getArtifactId() {
		return artifactId;
	}

	/** pom.xml の project.version */
	public String getVersion() {
		return version;
	}

	/** war/jarを作成したユーザ */
	public String getBuildBy() {
		return buildBy;
	}

	/** war/jarを作成したJDK */
	public String getBuildJdk() {
		return buildJdk;
	}


	/** war/jarを作成した時刻 */
	public String getBuildTimestamp() {
		return buildTimestamp;
	}
}
