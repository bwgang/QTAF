package cn.baiweigang.qtaf.toolkit.file;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.baiweigang.qtaf.toolkit.log.Tklogger;

//2007以下版本xsl
//2007及以上版本xslx

/**
 * Excel文件的读写
 * @author @<a href='http://weibo.com/bwgang'>bwgang</a><br/>
 *
 * 2007以下版本xsl
 * 2007及以上版本xslx
 */
public class TkExcel {
	private static Tklogger log=Tklogger.getLogger(TkExcel.class);//记录日志
	
	private Workbook wb;//工作簿
	private Sheet sheet;//sheet表
	private String pathName;
	
	public TkExcel(){
		
	}
	public TkExcel(String pathName){
		this.pathName=pathName;
		setWb();
	}
	public TkExcel(String pathName,String sheetName){
		this.pathName=pathName;
		setWb();
		setSheet(sheetName);
	}
	public TkExcel(String pathName,int sheetIndex){
		this.pathName=pathName;
		setWb();
		setSheet(sheetIndex);
	}
	public void writeExcel2007(XSSFWorkbook wb,String pathName) {
		try {
			wb.write(TkFile.getFileOutStream(pathName));
		} catch (IOException e) {
			log.error("写入Excel文件失败："+pathName);
			log.error(e.getMessage());
		}
	}
	
	public Workbook getWb() {
		return this.wb;
	}
	/**
	 * 设置Excel文件路径
	 * @param pathName
	 */
	public void setPathName(String pathName) {
		this.pathName=pathName;
		setWb();
//		setSheet();
	}
//	public void setSheet() {
//		try {
//			setSheet(wb.getSheetAt(0));
//		} catch (Exception e) {
//			log.error("读取Excel文件失败："+this.pathName);
//			log.error(e.getMessage());
//		}
//		
//	}
	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
		//删除掉空白行
		delBankRow();
	}
	public void setSheet(String sheetName) {
		if (null==wb) return;
		setSheet(getWb().getSheet(sheetName));
		if (null==getSheet()) {//按名称未获取到sheet
//			log.info("Excel："+this.pathName+"中，要获取的名称为"+sheetName+"的sheet页不存在，" +
//					"默认获取第一个sheet页");
			setSheet(getWb().getSheetAt(0));
			if (null==getSheet())log.error("Excel："+this.pathName+"中，不存在任何Sheet");
		}
	}
	public void setSheet(int sheetIndex) {
		if (null==wb) return;
		try {
			setSheet(getWb().getSheetAt(sheetIndex));
		} catch (Exception e) {
			log.error("Excel："+this.pathName+"中，要获取的第"+sheetIndex+"个sheet不存在,"
					+"默认获取第一个Sheet1");
			setSheet(getWb().getSheetAt(0));
			if (null==getSheet())log.error("Excel："+this.pathName+"中，不存在任何Sheet");
		}
		
	}
	/**
	 * 获取单元格内容，坐标从(0,0)开始，横为行，竖为列
	 * @param rowIndex
	 * @param colIndex
	 * @return
	 */
	public String getCellValue(int rowIndex,int colIndex) {
		if ( colIndex<0) {
			log.error("读取Excel的列数不能为负数");
			return null;
		}
		Row row=getRow(rowIndex);
		if (null==row) return null;
 		return getStrFromCell(row.getCell(colIndex));
	}
	/**
	 * 获取一行的内容
	 * @param rowIndex
	 * @return
	 */
	public List<String> getRowList(int rowIndex) {
		List<String> reslist=new ArrayList<>();
		Row row=getRow(rowIndex);
		if (null!=row) {
			for (int i = 0; i < getColNum(); i++) {
				reslist.add(getCellValue(rowIndex, i));
			}
		}
		
		return reslist;
	}
	/**
	 * 获取行数
	 * @return
	 */
	public int getRowNum() {
		if (null==getSheet())return 0;
		return getSheet().getLastRowNum()+1;
	}
	/**
	 * 获取列数
	 * @return
	 */
	public int getColNum() {
		int max=0;
		if (null==getSheet())return 0;
		if (getSheet().getPhysicalNumberOfRows()<1)return 0;
		for (int i = 0; i < getRowNum(); i++) {
			int tmp=getRow(i).getPhysicalNumberOfCells();
//			log.error(i+"=="+tmp);
			if (tmp>=max)max=tmp;
		}
		return max;
	}

	private Row getRow(int rowIndex) {
		if ( rowIndex<0) {
			log.error("读取Excel的行数不能为负数");
			return null;
		}
		if (null==getSheet()) return null;
		Row row=getSheet().getRow(rowIndex);
		if (null==row) {
			if (rowIndex<getRowNum()) return null;
			log.info("读取Excel："+this.pathName+" 的表-"+getSheet().getSheetName()
					+" 的第"+rowIndex+"行失败");
			return null;
		}
		return row;
	}
	private String getStrFromCell(Cell cell) {
		String res = "";
		if (null==cell) {
			return "";
		}
//		res=cell.getRichStringCellValue().toString();
		
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC: // 数字/日期
				if (DateUtil.isCellDateFormatted(cell)){
					res=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cell.getDateCellValue());
				}else{
					BigDecimal value =  new BigDecimal(cell.getNumericCellValue());
					String str = value.toString(); 
					if(str.contains(".0"))str = str.replace(".0", "");
					res=str;
				}
				break;
			case Cell.CELL_TYPE_STRING: // 字符串
				res = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN: // 布尔
				Boolean booleanValue = cell.getBooleanCellValue();
				res = booleanValue.toString();
				break;
			case Cell.CELL_TYPE_BLANK: // 空值
				res = "";
				break;
			case Cell.CELL_TYPE_FORMULA: // 公式
				res = cell.getCellFormula();
				break;
			case Cell.CELL_TYPE_ERROR: // 故障
				res = "";
				break;
			default:
				System.out.println("未知类型");
				break;
		}
		return res;
	}
	private Sheet getSheet(){
		return this.sheet;
	}
	private void setWb() {
		try {
			if (null==this.pathName) return ;
			String exname=TkFile.getExtensionName(this.pathName);
			if (exname.indexOf("xls")>-1 && exname.indexOf("xlsx")<0) {
				this.wb=new HSSFWorkbook(TkFile.readToFileInputStream(this.pathName));
			}else if (exname.indexOf("xlsx")>-1) {
				this.wb=new XSSFWorkbook(TkFile.readToFileInputStream(this.pathName));
			}else{
				log.info("无法读取，Excel文件异常："+this.pathName);
			}
		} catch ( NullPointerException | IOException e) {
			log.error("读取Excel文件出错："+this.pathName);
			log.error(e.getMessage());
		}
	}
	private void delBankRow() {
		for (int i = 0; i <= getRowNum(); i++) {
			Row r;
			try {
				r = sheet.getRow(i);
			} catch (Exception e) {
				r = null;
				// sheet.removeRowBreak(i);
				continue;
			}

			if (r == null && i == sheet.getLastRowNum()) {
				// 如果是空行,且到了最后一行,直接将那一行删掉
				sheet.removeRow(r);
			} else if (r == null && i < sheet.getLastRowNum()) {
				// 如果还没到最后一行，则数据往上移一行
				sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
			}

		}
	}
	public  void createBlankExcel2007(String pathName) {
		XSSFWorkbook wb=new XSSFWorkbook();
//		wb.createSheet("Sheet1");
		writeExcel2007(wb, pathName);
	}

	
	
//	/**
//	 * 取RGB颜色
//	 * @param c
//	 * @return
//	 */
//	private  String getColor(short c){
//		if(c<8||c>63){
//			return "";
//		}
//		String color = (HSSFColor.getIndexHash().get(new Integer(c))).getHexString();
//		String[] cs = color.split(":");
//		color = "#";
//		for(int j=0;j<cs.length;j++){
//			if(cs[j].length()==1){
//				color+=cs[j]+cs[j];
//			}else if(cs[j].length()==4){
//				color+=cs[j].substring(2);
//			}else{
//				color+=cs[j];
//			}
//		}
//		return color;
//	}
	
	/**
	 * Excel Sheet转换为html
	 * @param sheet
	 * @param workbook 
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
//	public  String ConvertToHtml(){
//		int row = getRowNum();
//		Row _row = getSheet().getRow(0);
//		int col =  getColNum();
//		String[][][] tdinfo = new String[row][col][2];
//		Map style = new HashMap();
//		// 合并单元格
//		for(int i=0;i<getSheet().getNumMergedRegions();i++){
//			HSSFSheet sheet1=(HSSFSheet)getSheet();
//			@SuppressWarnings("deprecation")
//			Region m = sheet1.getMergedRegionAt(i);
//			int rs = m.getRowFrom();
//			int re = m.getRowTo();
//			int cs = m.getColumnFrom();
//			int ce = m.getColumnTo();
//			tdinfo[rs][cs][0] = "";
//			if(re > rs){
//				tdinfo[rs][cs][0] += " rowspan='"+(re-rs+1)+"'";
//			}
//			if(ce>cs){
//				tdinfo[rs][cs][0] += " colspan='"+(ce-cs+1)+"'";
//			}
//			for(int x=rs;x<=re;x++){
//				for(int y=cs;y<=ce;y++){
//					if(x!=rs || y!=cs){
//						tdinfo[x][y] = null;
//					}
//				}
//			}
//		}
//		float[] width = new float[col];
//		int widthsum = 0;
//		int max = 0;
//		// 列宽
//		for(int i=0;i<col;i++){
//			width[i] = getSheet().getColumnWidth((short) i);
//			if(width[i]>=width[max]){
//				max = i;
//			}
//			widthsum += width[i];
//		}
//		// 最宽的一列不指定宽度
//		width[max] = 0;
//		// 设置单元格内容
//		for(int i=0;i<row;i++){
//			_row = getRow(i);
//			for(int j=0;j<col;j++){
//				if(tdinfo[i][j] == null){
//					continue;
//				}
//				Cell cell = _row.getCell((short) j);
//				if(cell != null){
//					CellStyle s = cell.getCellStyle();
//					if(tdinfo[i][j][0] == null){
//						tdinfo[i][j][0] = "";
//					}
//					// 设置单元格的样式
//					tdinfo[i][j][0] += " class='"+getCssByStyle(s,getWb(),style)+"'";
//					// 设置单元格的值
//					tdinfo[i][j][1] = getCellValue(i, j);					
//				}else{
//					tdinfo[i][j] = null;
//				}
//			}
//		}
//				
//		StringBuffer br = new StringBuffer();
//		br.append("<html xmlns:o='urn:schemas-microsoft-com:office:office' xmlns:x='urn:schemas-microsoft-com:office:excel' xmlns='http://www.w3.org/TR/REC-html40'>");
//		br.append("<head><meta http-equiv=Content-Type content='text/html; charset=utf-8'><meta name=ProgId content=Excel.Sheet>");
//		br.append("<style>");
//		Iterator it = style.values().iterator();
//		while(it.hasNext()){
//			String[] css = (String[])it.next();
//			br.append(css[1]);
//		}
//		br.append("</style></head><body>");
//		br.append("<table cellspacing='0' cellpadding='0' style='border-collapse:collapse;'>");
//		// 设置单元格的宽度
//		for(int i=0;i<col;i++){
//			if(i != max){
//				br.append("<col width='"+Math.rint(width[i]/widthsum*100)+"%'>");
//			}else{
//				br.append("<col>");
//			}
//		}
//		for(int i=0;i<row;i++){
//			br.append("<tr>");
//			for(int j=0;j<col;j++){
//				if(tdinfo[i][j] != null){
//					if(tdinfo[i][j][0] == null){
//						tdinfo[i][j][0] = "";
//					}
//					if(tdinfo[i][j][1]==null){
//						tdinfo[i][j][1] = " ";
//					}
//					br.append("<td "+tdinfo[i][j][0]+">"+tdinfo[i][j][1]+"</td>");
//				}
//			}
//			br.append("</tr>");
//		}
//		br.append("</table></body></html>");
//		return br.toString();
//	
//	}
//	
	
//	private  String getCssByStyle(CellStyle s, Workbook workbook,Map style) {
//		if(style.containsKey(s)){
//			String[] css = (String[])style.get(s);
//			return css[0];
//		}else{
//			String[] css = new String[2];
//			css[0] = "c"+style.size();
//			StringBuffer cssinfo = new StringBuffer();
//			// 文字对齐方式
//			switch(s.getAlignment()){
//				case CellStyle.ALIGN_CENTER:
//					cssinfo.append("text-align:center;");break;
//				case CellStyle.ALIGN_LEFT:
//					cssinfo.append("text-align:left;");break;
//				case CellStyle.ALIGN_RIGHT:
//					cssinfo.append("text-align:right;");break;
//			}
//			// 背景色
//			cssinfo.append("background-color:#D1DFF0;");
//			log.error(s.getFillBackgroundColor());
////			s.getFillForegroundColorColor().toString();
////			cssinfo.append("background-color:"+getColor(s.getFillForegroundColor())+";");
//			// 设置边框
//			cssinfo.append("border-top:"+s.getBorderTop()+"px solid #000000;");
//			cssinfo.append("border-left:"+s.getBorderLeft()+"px solid #000000;");
//			cssinfo.append("border-right:"+s.getBorderRight()+"px solid #000000;");
//			cssinfo.append("border-bottom:"+s.getBorderBottom()+"px solid #000000;");
//			// 设置字体
//			Font font = workbook.getFontAt(s.getFontIndex());
//			cssinfo.append("font-size:"+font.getFontHeightInPoints()+"pt;");
//			if(Font.BOLDWEIGHT_BOLD == font.getBoldweight()){
//				cssinfo.append("font-weight: bold;");
//			}
//			cssinfo.append("font-family: "+font.getFontName()+";");
//			if(font.getItalic()){
//				cssinfo.append("font-style: italic;");
//			}
//			String fontcolor = getColor(font.getColor());{
//				if(fontcolor.trim().length() > 0){
//					cssinfo.append("color: "+fontcolor+";");
//				}
//			}
//			css[1] = "."+css[0]+"{"+cssinfo.toString()+"}";
//			style.put(s, css);
//			return css[0];
//		}
//	}

	
}
