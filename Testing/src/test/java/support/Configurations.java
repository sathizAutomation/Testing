package support;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
/**
 * File name   :Configurations.java
 * Description  : 
 * Date created :13 Sep 2021
 * Author 		:Sathish
 */
public class Configurations {
    public static Configurations configurations;
    public Properties properties;
    public Settings settings = Settings.getInstance();
    public Configurations() {};
    public static Configurations getInstance() {
        if (configurations == null) {
            configurations = new Configurations();
        }
        return configurations;
    }
    /**
     * 
     * Method name  : setConfigurationProperties
     * Return types : void
     * Description  :
     */
    public void setConfigurationProperties() {
        String confFile = settings.getConfigurationProp();
        properties = new Properties();
        try {
            InputStream inStream = new FileInputStream(confFile);
            properties.load(inStream);
        } catch (Exception e) {
            new Exception("Error in reading configuration file!!!");
        }
    }
    /**
     * 
     * Method name  : getProperty
     * Return types : String
     * Description  :
     */
    public String getProperty(String property) {
        if (properties == null) {
            setConfigurationProperties();
        }
        if (properties.getProperty(property) != null) {
            return properties.getProperty(property);
        } else {
            return "No such property";
        }
    }
}