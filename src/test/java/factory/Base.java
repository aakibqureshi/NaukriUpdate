package factory;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Base {

    static WebDriver driver;
    static Properties p;

    public static WebDriver initilizeBrowser() throws IOException {
        p = getProperties();
        String executionEnv = p.getProperty("execution_env");
        String browser = p.getProperty("browser").toLowerCase();
        String os = p.getProperty("os").toLowerCase();

        if (executionEnv.equalsIgnoreCase("remote")) {
            DesiredCapabilities capabilities = new DesiredCapabilities();

            // OS
            switch (os) {
                case "windows": capabilities.setPlatform(Platform.WINDOWS); break;
                case "mac": capabilities.setPlatform(Platform.MAC); break;
                case "linux": capabilities.setPlatform(Platform.LINUX); break;
                default: System.out.println("No matching OS"); return null;
            }

            // Browser
            switch (browser) {
                case "chrome":
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--headless=new");
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-dev-shm-usage");
                    options.addArguments("--disable-gpu");
                    options.addArguments("--remote-allow-origins=*");
                    capabilities.setBrowserName("chrome");
                    driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
                    break;
                case "edge":
                    capabilities.setBrowserName("MicrosoftEdge");
                    driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
                    break;
                case "firefox":
                    capabilities.setBrowserName("firefox");
                    driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities);
                    break;
                default:
                    System.out.println("No matching browser");
                    return null;
            }

        } else if (executionEnv.equalsIgnoreCase("local")) {
            switch (browser) {
                case "chrome":
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--headless=new");
                    options.addArguments("--no-sandbox");
                    options.addArguments("--disable-dev-shm-usage");
                    options.addArguments("--disable-gpu");
                    options.addArguments("--remote-allow-origins=*");
                    driver = new ChromeDriver(options); // Selenium Manager resolves driver
                    break;
                case "edge":
                    driver = new EdgeDriver(); // Selenium Manager resolves driver
                    break;
                case "firefox":
                    driver = new FirefoxDriver(); // Selenium Manager resolves driver
                    break;
                default:
                    System.out.println("No matching browser");
                    driver = null;
            }
        }

        if (driver != null) {
            driver.manage().deleteAllCookies();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }

        return driver;
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static Properties getProperties() throws IOException {
        FileReader file = new FileReader(System.getProperty("user.dir") + "//src/test//resources//config.properties");
        p = new Properties();
        p.load(file);
        return p;
    }
}
