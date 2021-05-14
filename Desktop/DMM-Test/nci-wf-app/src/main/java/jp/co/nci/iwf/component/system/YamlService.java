package jp.co.nci.iwf.component.system;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;

import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;

/**
 * YAML形式のファイルを読み込むためのサービス。
 */
@ApplicationScoped
public class YamlService {
	@Inject
	private Logger log;

	/**
	 * YAMLファイルを読み込んで、Map化して返す
	 * @param path
	 * @return
	 */
	public YamlMap read(String path) {
		// Yamlの読み込み
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
			final YamlMap map = new YamlMap(128);
			if (is == null) {
				log.warn("YAMLファイル '{}' が存在しません", path);
			}
			else {
				final DuplicateChecker checker = new DuplicateChecker();

				@SuppressWarnings("unchecked")
				final Map<String, Object> yaml = (Map<String, Object>)new Yaml(checker).load(is);

				if (!checker.getDuplicateKeys().isEmpty()) {
					String keys = String.join(", ", checker.getDuplicateKeys());
					throw new InternalServerErrorException("yaml[" + path + "]ファイルでキー重複[" + keys + "]");
				}
				for (String key : yaml.keySet()) {
					if (Thread.currentThread().isInterrupted())
						break;
					if (map.contains(key))
						throw new InternalServerErrorException("yaml[" + path + "]ファイルでキー重複[" + key + "]");
					Object value = yaml.get(key);
					if (value == null)
						value = "";
					map.put(key, value);
				}
			}
			return map;
		}
		catch (IOException e) {
			throw new WebApplicationException(e);
		}
	}


	/**
	 * YAMLファイルのエントリにキー重複がないかを調べるためのチェッカー
	 *
	 */
	private static class DuplicateChecker extends Constructor {

		// Duplicate key information
		private List<String> duplicateKeys = new ArrayList<>();

		@Override
		protected void constructMapping2ndStep(MappingNode mappingNode,
				Map<Object, Object> mapping) {
			List<NodeTuple> nodes = (List<NodeTuple>) mappingNode.getValue();

			for (NodeTuple node : nodes) {
				Node keyNode = node.getKeyNode();
				Node valueNode = node.getValueNode();
				Object key = constructObject(keyNode);
				Object value = constructObject(valueNode);
				Object oldValue = mapping.put(key, value);

				if (oldValue != null) {
					duplicateKeys.add(key.toString());
				}
			}
		}

		/**
		 * Get duplicate key information
		 *
		 * @return Duplicate keys
		 */
		public List<String> getDuplicateKeys() {
			return duplicateKeys;
		}
	}
}
