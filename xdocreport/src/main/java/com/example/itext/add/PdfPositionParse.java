package com.example.itext.add;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.itext.replace.ReplaceRegion;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;  
  
/** 
 * 解析PDF中文本的x,y位置 
 * @user : caoxu-yiyang@qq.com 
 * @date : 2016年11月9日 
 */  
public class PdfPositionParse {  
  
    private PdfReader reader;  
    private String keyWord;
    private PdfReaderContentParser parser;  
  
    public PdfPositionParse(String fileName) throws IOException{  
        FileInputStream in = null;  
        try{  
            in =new FileInputStream(fileName);  
            byte[] bytes = new byte[in.available()];  
            in.read(bytes);  
            init(bytes);  
        }finally{  
            in.close();  
        }  
    }  
      
    public PdfPositionParse(byte[] bytes) throws IOException{  
        init(bytes);  
    }  
      
    private boolean needClose = true;  
    /** 
     * 传递进来的reader不会在PdfPositionParse结束时关闭 
     * @user : caoxu-yiyang@qq.com 
     * @date : 2016年11月9日 
     * @param reader 
     */  
    public PdfPositionParse(PdfReader reader){  
        this.reader = reader;  
        parser = new PdfReaderContentParser(reader);  
        needClose = false;  
    }  
      
    private void init(byte[] bytes) throws IOException {  
        reader = new PdfReader(bytes);  
        parser = new PdfReaderContentParser(reader);  
    }  
      
    /** 
     * 解析文本 
     * @user : caoxu-yiyang@qq.com 
     * @date : 2016年11月9日 
     * @throws IOException 
     */  
    public List<ReplaceRegion> getRegions() throws IOException{  
        try{  
            List<ReplaceRegion> result = new ArrayList<ReplaceRegion>();
            int pageNum = reader.getNumberOfPages();  
            for (int i = 1; i <= pageNum; i++) {
            	PositionRenderListener listener = new PositionRenderListener(this.keyWord); 
            	parser.processContent(i, listener);  
            	List<ReplaceRegion> pageRresult = listener.getResult();
            	final Integer page = i;
            	pageRresult.forEach((ReplaceRegion region)->region.setPage(page));
            	result.addAll(pageRresult);
			} 
            return result;  
        }finally{  
            if(reader != null && needClose){  
                reader.close();  
            }  
        }  
    }

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}  
}