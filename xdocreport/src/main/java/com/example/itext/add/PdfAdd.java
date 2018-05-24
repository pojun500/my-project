package com.example.itext.add;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.itext.replace.ReplaceRegion;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * 替换PDF文件某个区域内的文本
 * 
 * @date : 2016年11月8日
 */
public class PdfAdd {
	private static final Logger logger = LoggerFactory.getLogger(PdfAdd.class);

	private int fontSize;
	private String keyWord;

	private Map<String, Object> replaceTextMap = new HashMap<String, Object>();
	private ByteArrayOutputStream output;
	private PdfReader reader;
	private PdfStamper stamper;
	private PdfContentByte canvas;
	private Font font;

	public PdfAdd(byte[] pdfBytes) throws DocumentException, IOException {
		init(pdfBytes);
	}

	public PdfAdd(String fileName) throws IOException, DocumentException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(fileName);
			byte[] pdfBytes = new byte[in.available()];
			in.read(pdfBytes);
			init(pdfBytes);
		} finally {
			in.close();
		}
	}

	private void init(byte[] pdfBytes) throws DocumentException, IOException {
		logger.info("初始化开始");
		reader = new PdfReader(pdfBytes);
		output = new ByteArrayOutputStream();
		stamper = new PdfStamper(reader, output);
		setFont(14);
		logger.info("初始化成功");
	}

	private void close() throws DocumentException, IOException {
		if (reader != null) {
			reader.close();
		}
		if (output != null) {
			output.close();
		}
		output = null;
	}


	public void replaceText(String name, String text) {
		this.replaceTextMap.put(name, text);
	}

	

	/**
	 * 生成新的PDF文件
	 * 
	 * @user : caoxu-yiyang@qq.com
	 * @date : 2016年11月9日
	 * @param fileName
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void toPdf(String fileName) throws DocumentException, IOException {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(fileName);
			fileOutputStream.write(output.toByteArray());
			fileOutputStream.flush();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
			close();
		}
		logger.info("文件生成成功");
	}

	/**
	 * 将生成的PDF文件转换成二进制数组
	 * 
	 * @user : caoxu-yiyang@qq.com
	 * @date : 2016年11月9日
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public byte[] toBytes() throws DocumentException, IOException {
		try {
			logger.info("二进制数据生成成功");
			return output.toByteArray();
		} finally {
			close();
		}
	}


	public int getFontSize() {
		return fontSize;
	}

	/**
	 * 设置字体大小
	 * 
	 * @user : caoxu-yiyang@qq.com
	 * @date : 2016年11月9日
	 * @param fontSize
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void setFont(int fontSize) throws DocumentException, IOException {
		if (fontSize != this.fontSize) {
			this.fontSize = fontSize;
			BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
			font = new Font(bf, this.fontSize, Font.BOLD);
		}
	}

	public void setFont(Font font) {
		if (font == null) {
			throw new NullPointerException("font is null");
		}
		this.font = font;
	}

	public static void main(String[] args) throws IOException, DocumentException {
		PdfAdd textReplacer = new PdfAdd("d://合同协议书.pdf");
		textReplacer.setKeyWord("签订日期");
		List<ReplaceRegion> regins = textReplacer.getRegins();
		ReplaceRegion replaceRegion = regins.get(0);
		float x = replaceRegion.getW()+replaceRegion.getX();
		replaceRegion.setX(x);
		//logger.info("要增加的页{},坐标x:{},坐标y:{}",replaceRegion.getPage(),replaceRegion.getX(),replaceRegion.getY());
		textReplacer.addText(replaceRegion, "盖章啦");
		textReplacer.toPdf("d://合同协议书2.pdf");
	}

	private void addText(ReplaceRegion replaceRegion, String value) throws DocumentException, IOException {
		try {
			canvas = stamper.getOverContent(replaceRegion.getPage());
			canvas.saveState();
			canvas.setColorFill(BaseColor.WHITE);
			canvas.rectangle(replaceRegion.getX(), replaceRegion.getY(), replaceRegion.getW(), replaceRegion.getH());
			canvas.fill();
			canvas.restoreState();
			// 开始写入文本
			canvas.beginText();
			// 设置字体
			canvas.setFontAndSize(font.getBaseFont(), getFontSize());
			canvas.setTextMatrix(replaceRegion.getX(), replaceRegion.getY() + 2/* 修正背景与文本的相对位置 */);
			canvas.showText(value);
			canvas.endText();

		} finally {
			if (stamper != null) {
				stamper.close();
			}
		}
	}

	public List<ReplaceRegion> getRegins() throws IOException {
		PdfPositionParse parse = new PdfPositionParse(reader);
		parse.setKeyWord(keyWord);
		return parse.getRegions();
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getKeyWord() {
		return keyWord;
	}

}