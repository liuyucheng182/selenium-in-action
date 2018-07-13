package me.zaing.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Main {

	/** 用户名 */
	private static String username = "13683283532";
	/** 密码 */
	private static String password = "19921229";
	/** 待读取的 Excel 文件 */
	private static String excelLocation = "D:/Excel";
	/** 创建销售品时使用的图片 */
	private static String imageLocation = "D:/123.jpg";
	/** 是否是生产环境，true:是，false:不是 */
	private static boolean isProduct = true;

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	private static String domain;
	private static WebDriver driver;
	static {
		if (isProduct) {
			domain = "http://ctrl.189.cn";
		} else {
			//domain = "http://118.85.207.95:8016";
			domain = "http://127.0.0.1:8080";
		}
		System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
		driver = new ChromeDriver();
	}

	public static void main(String[] args) {
		try {
			// 登录
			login();
			// 二次验证
			if (isProduct) { secondAuth(); }

			File[] excelFiles = new File(excelLocation).listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					return !file.isDirectory();
				}
			});

			for(File loc : excelFiles) {
				logger.debug("开始导入文件【{}】...", loc);
				try {
					List<Pojo> pojos = Util.parseExcel2Pojo(loc);
					for (Pojo pojo : pojos) {
						logger.debug("正在导入 {}", pojo);
						// 创建产品
						createBProduct(pojo);
						// 创建销售品
						createBGood(pojo);
						// TODO 每创建一个产品和销售品都保存一次
						Util.parsePojo2Excel(pojo);
					}
					logger.debug("导入文件【{}】成功，成功导入 {} 条数据。", loc, pojos.size());
				} catch (Exception e) {
					logger.error("导入文件【" + loc + "】时出现异常，停止导入后续文件。", e);
					break;
				}
			}
		}catch (Exception e) {
			logger.error("", e);
		} finally {
			//driver.close();
		}
	}

	/**
	 * 创建销售品
	 */
	private static void createBGood(Pojo pojo) {

		try {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("div.success"), "添加成功!"));

			driver.get(domain + "/ec-gms/goods/createCommonGoods?goodsABC=B");
			sleep(5);
			// wait.until(ExpectedConditions.attributeContains(By.tagName("body"), "class", "loading"));

			// 销售品类型
			Select xiaoShouPinLeiXing = new Select(driver.findElement(By.id("goodTsOfferType")));
			xiaoShouPinLeiXing.selectByVisibleText(pojo.getXiaoShouPinLeiXing());
			sleep(1);
			// 切换销售品类型后的弹框
			WebElement confirmBtn = driver.findElement(By.cssSelector(".dialog-button.messager-button > a:first-child"));
			confirmBtn.click();
			sleep(5);
			// wait.until(ExpectedConditions.attributeContains(By.tagName("body"), "class", "loading"));

			// 店铺
			Select dianPu = new Select(driver.findElement(By.id("goodShopTsAssetId")));
			dianPu.selectByVisibleText(pojo.getDianPu());
			sleep(5);
			// 选择产品
			WebElement xuanZeChanPinBtn = driver.findElement(By.cssSelector("[title='选择产品']"));
			xuanZeChanPinBtn.click(); // 打开选择产品对话框
			sleep(1);
			WebElement chanPinBianMaQueryParam = driver.findElement(By.id("productTsSpTxm"));
			chanPinBianMaQueryParam.sendKeys(pojo.getChanPinBianMa()); // 填写[产品编码]查询条件
			sleep(1);
			WebElement queryProductBtn = driver.findElement(By.cssSelector("[onclick='forQueryProduct()']"));
			queryProductBtn.click(); // 点击[查询]按钮
			sleep(5);
			WebElement chanPinRow = driver.findElement(By.cssSelector("[id^='datagrid-row-r']"));
			chanPinRow.click(); // 选择第一行
			sleep(1);
			WebElement confirmProductBtn = driver.findElement(By.cssSelector("[onclick='forConfirmProduct()']"));
			confirmProductBtn.click(); // 点击[确定]按钮
			sleep(1);
			// 销售品名称
			WebElement xiaoShouPinMingCheng = driver.findElement(By.cssSelector("#goodsTsMkTitle+span>#_easyui_textbox_input1"));
			xiaoShouPinMingCheng.sendKeys(pojo.getChangBiaoTi());
			sleep(1);
			// 是否隐藏
			WebElement shiFouYinCang = driver.findElement(By.id("tsTdy6"));
			shiFouYinCang.click();
			sleep(1);

			// 切换至[相册]Tab页
			WebElement xiangCeTab = driver.findElement(By.partialLinkText("相册"));
			xiangCeTab.click();
			sleep(1);
			// 选择图片
			WebElement image = driver.findElement(By.id("image"));
			image.sendKeys(imageLocation);
			sleep(1);
			// 上传图片
			WebElement uploadBtn = driver.findElement(By.cssSelector("[onclick^='document']"));
			uploadBtn.click();

			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("msg"), "上传成功!"));
			sleep(1);

			// 类型目录-宽带-宽带套餐
			// 切换至[类型目录]Tab页
			WebElement leiXingMuLuTab = driver.findElement(By.partialLinkText("类型目录"));
			leiXingMuLuTab.click();
			sleep(1);
			// 展开[宽带]
			WebElement kuanDai = driver.findElement(By.cssSelector("#_easyui_tree_30>span:nth-child(1)"));
			kuanDai.click();
			sleep(1);
			// 选中[宽带套餐]
			WebElement kuanDaiTaoCan = driver.findElement(By.cssSelector("#_easyui_tree_31>span:nth-child(4)"));
			kuanDaiTaoCan.click();
			sleep(5);
			List<WebElement> tsKxzList = driver.switchTo().frame("queryTypeDirectory").findElements(By.cssSelector("[name='tsKxzMr']"));
			// 类型
			new Select(tsKxzList.get(0)).selectByVisibleText(pojo.getLeiXing());
			sleep(1);
			// 网速
			/*if (pojo.getWangSu() == null || "".equals(pojo.getWangSu())) {
				input("请选择【宽带-宽带套餐-网速】，选择完成后请回来敲回车");
			} else {*/
			new Select(tsKxzList.get(1)).selectByVisibleText(pojo.getWangSu());
			//}
			sleep(1);
			// 价格
			new Select(tsKxzList.get(2)).selectByVisibleText(pojo.getJiaGe());
			sleep(1);
			// 时长
			/*if (pojo.getShiChang() == null || "".equals(pojo.getShiChang())) {
				input("请选择【宽带-宽带套餐-时长】，选择完成后请回来敲回车");
			} else {*/
			new Select(tsKxzList.get(3)).selectByVisibleText(pojo.getShiChang());
			// }
			sleep(1);

			// 保存数据
			driver.switchTo().defaultContent();
			WebElement saveBtn = driver.findElement(By.id("button1"));
			saveBtn.click();

			// 上架
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("div.success"), "保存成功"));
			WebElement shangJiaBtn = driver.findElement(By.partialLinkText("上架"));
			shangJiaBtn.click();
			wait.until(ExpectedConditions.textToBePresentInElementLocated(By.cssSelector("div.success"), "销售品上架成功！"));
			// 记录销售品编码
			String xiaoShouPinBianMa = driver.findElement(By.cssSelector("table[id='stupidtable'] > tbody > tr:nth-child(1) > td")).getText();
			pojo.setXiaoShouPinBianMa(xiaoShouPinBianMa);
			logger.debug("保存销售品【{}】成功", pojo.getChangBiaoTi());
		} catch (Exception e) {
			logger.error("保存销售品【{}】失败：\n{}", pojo.getChangBiaoTi(), e);
			throw e;
		}
	}

	/**
	 * 创建产品
	 */
	private static void createBProduct(Pojo pojo) {
		try {
			driver.get(domain + "/ec-gms/product/createDeviceProduct?productABC=B");
			sleep(3);
			// 产品编码
			WebElement chanPinBianMa = driver.findElement(By.id("productTsSpTxm"));
			chanPinBianMa.sendKeys(pojo.getChanPinBianMa());
			sleep(2);
			// 产品名称
			WebElement chanPinMingCheng = driver.findElement(By.id("productTsSpName"));
			chanPinMingCheng.sendKeys(pojo.getChanPinMingCheng());
			sleep(1);
			// 店铺
			Select dianPu = new Select(driver.findElement(By.id("productShop")));
			dianPu.selectByVisibleText(pojo.getDianPu());
			sleep(1);
			// 产品类型
			Select chanPinLeiXing = new Select(driver.findElement(By.id("productType")));
			chanPinLeiXing.selectByVisibleText(pojo.getChanPinLeiXing());
			sleep(1);
			// 产品NBR
			WebElement chanPinNBR = driver.findElement(By.id("productNbr"));
			chanPinNBR.sendKeys(pojo.getChanPinNBR());
			sleep(1);
			// 长标题
			WebElement changBiaoTi = driver.findElement(By.id("tsLongTitle"));
			changBiaoTi.sendKeys(pojo.getChangBiaoTi());
			sleep(1);
			// 所属品牌
			Select suoShuPinPai = new Select(driver.findElement(By.id("brand")));
			if (isProduct)
				suoShuPinPai.selectByVisibleText(pojo.getSuoShuPinPai());
			else {
				suoShuPinPai.selectByVisibleText(pojo.getSuoShuPinPai());
			}
			sleep(1);
			// 市场价
			WebElement shiChangJia = driver.findElement(By.id("_easyui_textbox_input4"));
			shiChangJia.sendKeys(pojo.getShiChangJia());
			sleep(1);
			// 售价
			WebElement shouJia = driver.findElement(By.id("_easyui_textbox_input3"));
			shouJia.sendKeys(pojo.getShouJia());
			sleep(1);
			// 保存数据按钮
			WebElement saveBtn = driver.findElement(By.cssSelector("[type=button]"));
			saveBtn.click();
			logger.debug("保存产品【{}】成功", pojo.getChangBiaoTi());
		} catch (Exception e) {
			logger.error("保存产品【{}】失败：\n{}", pojo.getChangBiaoTi(), e);
			throw e;
		}
	}

	/**
	 * 二次验证-手机验证码
	 */
	private static void secondAuth() {
		// 手机验证码
		WebElement phoneAuthCode = driver.findElement(By.id("password"));
		phoneAuthCode.sendKeys(input("手机验证码"));

		// 登录
		WebElement loginBtn = driver.findElement(By.id("loginBtn"));
		loginBtn.click();
	}

	/**
	 * 登录
	 */
	private static void login() {

		driver.get(domain + "/ec-gms/main");
		// 移动/联通手机号登录
		WebElement cmcloginbtn = driver.findElement(By.id("cmcloginbtn"));
		cmcloginbtn.click();

		// 用户名
		WebElement txtAccount = driver.findElement(By.id("txtAccount"));
		txtAccount.sendKeys(username);

		boolean flag;
		do {
			flag = true;

			// 密码
			WebElement txtShowPwd = driver.findElement(By.id("txtShowPwd"));
			txtShowPwd.click();
			WebElement txtPassword = driver.findElement(By.id("txtPassword"));
			txtPassword.sendKeys(password);

			// 验证码
			WebElement txtCaptcha = driver.findElement(By.id("txtCaptcha"));
			txtCaptcha.sendKeys(input("图片验证码"));

			// 登录按钮
			WebElement loginbtn = driver.findElement(By.id("loginbtn"));
			loginbtn.click();

			try {
				driver.findElement(By.id("divErr"));
			} catch (Exception e) {
				flag = false;
			}
		} while (flag);
	}

	private static String input(String desc) {
		Scanner sc = new Scanner(System.in);
		System.out.println(desc + ":");
		return sc.nextLine();
	}

	private static void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
