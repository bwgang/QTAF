package cn.baiweigang.qtaf.dispatch.report;

/**
 * 一个testng的测试集或测试套的执行结果信息
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/>
 */
public class TngCount {
	private String name;//测试集/测试套名称
	private String suiteName;//所属的测试套的名称，为空标识无所属测试套
	private int failed;
	private int passed;
	private int skipped;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSuiteName() {
		return suiteName;
	}
	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}
	public int getFailed() {
		return failed;
	}
	public void setFailed(int failed) {
		this.failed = failed;
	}
	public int getPassed() {
		return passed;
	}
	public void setPassed(int passed) {
		this.passed = passed;
	}
	public int getSkipped() {
		return skipped;
	}
	public void setSkipped(int skipped) {
		this.skipped = skipped;
	}
}
