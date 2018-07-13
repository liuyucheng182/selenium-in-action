package me.zaing.selenium;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

	public static void main(String[] args) throws Exception {
		 // parseExcel2Pojo(Thread.currentThread().getContextClassLoader().getResourceAsStream("HuNanTest.xlsx"));
		// test("宽1年带12个月预存1900元送年费380元（100M单宽包年） ");
		// setDestExcelLocation("D:/abc/def/123.txt");
		System.out.println(File.separator);
	}

	private static void test(String str) {
		Pattern pattern = Pattern.compile("(\\d|半)+(年|个月)|包.*?(年|个月)|年费");
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			System.out.println(matcher.group());
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(Util.class);
	private static String destExcelLocation;
	private static final Map<String, String> YEAR = new HashMap<>();
	static {
		YEAR.put("1个月","1个月");
		YEAR.put("3个月","3个月");

		YEAR.put("1年","1年");
		YEAR.put("12个月","1年");
		YEAR.put("包年","1年");
		YEAR.put("包1年","1年");
		YEAR.put("包一年","1年");
		YEAR.put("年费","1年");

		YEAR.put("2年","2年");
		YEAR.put("24个月","2年");
		YEAR.put("包2年","2年");
		YEAR.put("包两年","2年");
	}

	public static String parseKuanDaiWangSu(String name) {
		Pattern pattern = Pattern.compile("\\d+M");
		Matcher matcher = pattern.matcher(name);
		String wangSu = "4M";
		if (matcher.find()) {
			wangSu = matcher.group();
			switch (wangSu) {
				case "2M":
					break;
				case "4M":
					break;
				case "8M":
					break;
				case "10M":
					break;
				case "12M":
					break;
				case "20M":
					break;
				case "30M":
					break;
				case "50M":
					break;
				case "100M":
					break;
				default:
					wangSu = "4M";
					logger.debug("销售品【{}】的网速不在下拉选择框内，使用默认网速：{}", name, wangSu);
					break;
			}
		} else {
			logger.debug("销售品【{}】没有发现网速，使用默认网速：{}", name, wangSu);
		}
		return wangSu;
	}

	public static String parseKuanDaiShiChang(String name) {
		Pattern pattern = Pattern.compile("(\\d|半)+(年|个月)|包.*?(年|个月)|年费");
		Matcher matcher = pattern.matcher(name);
		String shiChang = "1年";
		if (matcher.find()) {
			shiChang = YEAR.get(matcher.group());
			// System.out.println(shiChang + " " + YEAR.get(shiChang));
			switch (shiChang == null ? "NULL" : shiChang) {
				case "1个月":
					break;
				case "3个月":
					break;
				case "1年":
					break;
				case "2年":
					break;
				default:
					shiChang = "1年";
					logger.debug("销售品【{}】的时长不在下拉选择框内，使用默认时长：{}", name, shiChang);
					break;
			}
		} else {
			logger.debug("销售品【{}】没有发现时长，使用默认时长：{}", name, shiChang);
		}
		return shiChang;
	}

	public static List<Pojo> parseExcel2Pojo(File excelFile) throws Exception{
		setDestExcelLocation(excelFile);
		try {
			Workbook workbook = new XSSFWorkbook(new FileInputStream(excelFile));
			List<List<String>> list = getCellValOfSheet(workbook.getSheetAt(0));
			return toPojoOfSheet(list);
		} catch (IOException e) {
			throw e;
		}
	}

	private static List<Pojo> toPojoOfSheet(List<List<String>> list) {
		List<Pojo> pojoList = new ArrayList<>();
		for (List<String> l : list) {
			pojoList.add(toPojoOfRow(l));
		}
		return pojoList;
	}

	private static Pojo toPojoOfRow(List<String> l) {
		Pojo pojo = new Pojo();
		for (int i = 0; i < l.size(); i++) {
			switch (i) {
				case 0: // 店铺
					pojo.setDianPu(l.get(i) + "省");
					break;
				case 1: // 地市
					pojo.setDiShi(l.get(i));
					break;
				case 2: // 产品NBR
					pojo.setChanPinNBR(l.get(i));
					break;
				case 4: // 长标题
					pojo.setChangBiaoTi(l.get(i));
					break;
				case 5: // 市场价、售价
					pojo.setShiChangJia(l.get(i));
					pojo.setShouJia(l.get(i));
					break;
			}
		}
		// 产品编码
		pojo.setChanPinBianMa(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
		// 网速
		pojo.setWangSu(parseKuanDaiWangSu(pojo.getChangBiaoTi()));
		// 价格
		String jiaGeStr = null;
		Double jiaGeDou = Double.valueOf(pojo.getShouJia());
		if (jiaGeDou >= 3000) {
			jiaGeStr = "3000元以上";
		} else if (jiaGeDou >= 2000) {
			jiaGeStr = "2000-2999元";
		} else if (jiaGeDou >= 1000) {
			jiaGeStr = "1000-1999元";
		} else if (jiaGeDou >= 500) {
			jiaGeStr = "500-999元";
		} else {
			jiaGeStr = "0-499元";
		}
		pojo.setJiaGe(jiaGeStr);
		// 时长
		pojo.setShiChang(parseKuanDaiShiChang(pojo.getChangBiaoTi()));
		return pojo;
	}

	private static List<List<String>> getCellValOfSheet(Sheet sheet) {
		List<List<String>> list = new ArrayList<>();
		for (int r = hasTitleLine(sheet) ? 1 : 0; r <= sheet.getLastRowNum(); r++) {
			list.add(getCellValOfRow(sheet.getRow(r)));
		}
		return list;
	}

	private static List<String> getCellValOfRow(Row row) {
		List<String> cellValOfRow = new ArrayList<>();
		Iterator<Cell> iterator = row.iterator();
		while (iterator.hasNext()) {
			cellValOfRow.add(getCellValue(iterator.next()));
		}
		return cellValOfRow;
	}

	/**
	 * 是否有标题行
	 */
	private static boolean hasTitleLine(Sheet sheet) {
		return "省份".equals(getCellValue(sheet.getRow(0).getCell(0)));
	}

	/**
	 * 获取单元格的值
	 */
	public static String getCellValue(Cell cell) {
		if (cell == null || cell.getCellTypeEnum().equals(CellType.BLANK))
			return "";
		if (cell.getCellTypeEnum().equals(CellType.BOOLEAN)) {
			return String.valueOf(cell.getBooleanCellValue());
		} else if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
			if (HSSFDateUtil.isCellDateFormatted(cell)) { // 日期必须要先判断为数字，否则出错
				Date d = cell.getDateCellValue();
				return formatDate(d, "yyyy/MM/dd HH:mm:ss");
			} else {
				return new DecimalFormat("0.00").format(cell.getNumericCellValue());
			}
		} else {
			return cell.getStringCellValue();
		}
	}

	/**
	 * 将日期转换字符串
	 *
	 * @param date
	 * @param format 格式
	 * @return
	 */
	public static String formatDate(Date date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	public static void parsePojo2Excel(Pojo pojo) throws Exception {
		try {
			File file = new File(destExcelLocation);
			Workbook workbook = null;
			Sheet sheet = null;
			Row row = null;
			Cell cell = null;
			int rowNum = -1;
			if (!file.exists()) {
				file.getParentFile().mkdirs(); file.createNewFile();
				workbook = new XSSFWorkbook();
				sheet = workbook.createSheet();
				row = sheet.createRow(0);
				// 省份
				cell = row.createCell(0);
				cell.setCellValue("省份");
				// 地市
				cell = row.createCell(1);
				cell.setCellValue("地市");
				// NBR（省销售品ID）
				cell = row.createCell(2);
				cell.setCellValue("NBR（省销售品ID）");
				// 集团产品编码
				cell = row.createCell(3);
				cell.setCellValue("集团产品编码");
				// 套餐内容（省填写）
				cell = row.createCell(4);
				cell.setCellValue("套餐内容（省填写）");
				// 价格
				cell = row.createCell(5);
				cell.setCellValue("价格");
				// 集团销售品编码（集团填写）
				cell = row.createCell(6);
				cell.setCellValue("集团销售品编码（集团填写）");

				rowNum = 1;
			} else {
				workbook = new XSSFWorkbook(new FileInputStream(file));
				sheet = workbook.getSheetAt(0);
				rowNum = sheet.getLastRowNum() + 1;
			}


			row = sheet.createRow(rowNum);
			// 省份
			cell = row.createCell(0);
			String dianPu = pojo.getDianPu();
			cell.setCellValue(dianPu.substring(0, dianPu.length() - 1));
			// 地市
			cell = row.createCell(1);
			cell.setCellValue(pojo.getDiShi());
			// NBR（省销售品ID）
			cell = row.createCell(2);
			cell.setCellValue(pojo.getChanPinNBR());
			// 集团产品编码
			cell = row.createCell(3);
			cell.setCellValue(pojo.getChanPinBianMa());
			// 套餐内容（省填写）
			cell = row.createCell(4);
			cell.setCellValue(pojo.getChangBiaoTi());
			// 价格
			cell = row.createCell(5);
			cell.setCellValue(pojo.getShouJia());
			// 集团销售品编码（集团填写）
			cell = row.createCell(6);
			cell.setCellValue(pojo.getXiaoShouPinBianMa());

			OutputStream os = new FileOutputStream(file);
			workbook.write(os);
			os.flush();
			os.close();
			workbook.close();
			logger.debug("销售品【{}】写入 Excel 文件成功", pojo.getChangBiaoTi());
		} catch (IOException e) {
			logger.error("销售品【{}】写入 Excel 文件失败：\n{}", pojo.getChangBiaoTi(), e);
			throw e;
		}
	}

	public static void setDestExcelLocation(File excelFile) {

		File file = new File(excelFile.getParentFile(), "NewExcel");
		file.mkdirs();

		Util.destExcelLocation = file.getAbsolutePath() + File.separator + "NewExcel.xlsx";
	}
}
