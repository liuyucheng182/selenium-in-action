package me.zaing.selenium;

public class Pojo {
	/** 产品编码 */
	private String chanPinBianMa;
	/** 产品名称 */
	private String chanPinMingCheng = "宽带产品";
	/** 店铺 */
	private String dianPu;
	/** 产品类型 */
	private String chanPinLeiXing = "上网卡";
	/** 产品NBR */
	private String chanPinNBR;
	/** 长标题 */
	private String changBiaoTi;
	/** 所属品牌 */
	private String suoShuPinPai = "其他品牌";
	/** 市场价 */
	private String shiChangJia;
	/** 售价 */
	private String shouJia;

	/** 销售品名称 */
	private String xiaoShouPinMingCheng;
	/** 销售品类型 */
	private String xiaoShouPinLeiXing = "B类单宽带";
	/** 店铺 */
	/** 产品名称，弹窗选择 */
	/** 市场价，选完产品自动会填写 */
	/** 销售价，选完产品自动会填写 */
	/** 长标题，选完产品会自动填写*/
	/** 是否隐藏 */
	private boolean shiFouYinCang = true;


	/***** 类型目录 *****/
	/** 宽带-宽带套餐 */
	/** 类型 */
	private String leiXing = "单宽带";
	/** 网速 */
	private String wangSu;
	/** 价格 */
	private String jiaGe;
	/** 时长 */
	private String shiChang;

	/***** 相册 *****/


	private String diShi;
	private String xiaoShouPinBianMa;

	public String getXiaoShouPinBianMa() {
		return xiaoShouPinBianMa;
	}

	public void setXiaoShouPinBianMa(String xiaoShouPinBianMa) {
		this.xiaoShouPinBianMa = xiaoShouPinBianMa;
	}

	public String getDiShi() {
		return diShi;
	}

	public void setDiShi(String diShi) {
		this.diShi = diShi;
	}

	public String getChanPinBianMa() {
		return chanPinBianMa;
	}

	public void setChanPinBianMa(String chanPinBianMa) {
		this.chanPinBianMa = chanPinBianMa;
	}

	public String getChanPinMingCheng() {
		return chanPinMingCheng;
	}

	public void setChanPinMingCheng(String chanPinMingCheng) {
		this.chanPinMingCheng = chanPinMingCheng;
	}

	public String getDianPu() {
		return dianPu;
	}

	public void setDianPu(String dianPu) {
		this.dianPu = dianPu;
	}

	public String getChanPinLeiXing() {
		return chanPinLeiXing;
	}

	public void setChanPinLeiXing(String chanPinLeiXing) {
		this.chanPinLeiXing = chanPinLeiXing;
	}

	public String getChanPinNBR() {
		return chanPinNBR;
	}

	public void setChanPinNBR(String chanPinNBR) {
		this.chanPinNBR = chanPinNBR;
	}

	public String getChangBiaoTi() {
		return changBiaoTi;
	}

	public void setChangBiaoTi(String changBiaoTi) {
		this.changBiaoTi = changBiaoTi;
	}

	public String getSuoShuPinPai() {
		return suoShuPinPai;
	}

	public void setSuoShuPinPai(String suoShuPinPai) {
		this.suoShuPinPai = suoShuPinPai;
	}

	public String getShiChangJia() {
		return shiChangJia;
	}

	public void setShiChangJia(String shiChangJia) {
		this.shiChangJia = shiChangJia;
	}

	public String getShouJia() {
		return shouJia;
	}

	public void setShouJia(String shouJia) {
		this.shouJia = shouJia;
	}

	public String getXiaoShouPinMingCheng() {
		return xiaoShouPinMingCheng;
	}

	public void setXiaoShouPinMingCheng(String xiaoShouPinMingCheng) {
		this.xiaoShouPinMingCheng = xiaoShouPinMingCheng;
	}

	public String getXiaoShouPinLeiXing() {
		return xiaoShouPinLeiXing;
	}

	public void setXiaoShouPinLeiXing(String xiaoShouPinLeiXing) {
		this.xiaoShouPinLeiXing = xiaoShouPinLeiXing;
	}

	public boolean isShiFouYinCang() {
		return shiFouYinCang;
	}

	public void setShiFouYinCang(boolean shiFouYinCang) {
		this.shiFouYinCang = shiFouYinCang;
	}

	public String getLeiXing() {
		return leiXing;
	}

	public void setLeiXing(String leiXing) {
		this.leiXing = leiXing;
	}

	public String getWangSu() {
		return wangSu;
	}

	public void setWangSu(String wangSu) {
		this.wangSu = wangSu;
	}

	public String getJiaGe() {
		return jiaGe;
	}

	public void setJiaGe(String jiaGe) {
		this.jiaGe = jiaGe;
	}

	public String getShiChang() {
		return shiChang;
	}

	public void setShiChang(String shiChang) {
		this.shiChang = shiChang;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Pojo { ").append("名称='" + changBiaoTi + '\'').append(", 店铺='" + dianPu + '\'').append(", 产品NBR='" + chanPinNBR + '\'')
			.append(chanPinBianMa != null ? ", 产品编码='" + chanPinBianMa + '\'' : "")
			.append(xiaoShouPinBianMa != null ? ", 销售品编码='" + xiaoShouPinBianMa + '\'' : "")
			.append(" }");
		return  sb.toString();
	}
}
