package com.example.itext.add;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.example.itext.replace.ReplaceRegion;
import com.itextpdf.awt.geom.Rectangle2D.Float;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

/**
 * pdf渲染监听,当找到渲染的文本时，得到文本的坐标x,y,w,h
 * 
 */
public class PositionRenderListener implements RenderListener {

	private String keyWord;
	private List<ReplaceRegion> result = new LinkedList<ReplaceRegion>();
	private float defaultH; /// 出现无法取到值的情况，默认为12
	private float fixHeight; // 可能出现无法完全覆盖的情况，提供修正的参数，默认为2

	public PositionRenderListener(String keyWord, float defaultH, float fixHeight) {
		this.keyWord = keyWord;
		this.defaultH = defaultH;
		this.fixHeight = fixHeight;
	}

	public PositionRenderListener(String keyWord) {
		this.keyWord = keyWord;
		this.defaultH = 12;
		this.fixHeight = 2;
	}

	public List<ReplaceRegion> getResult() {
		return this.result;
	}

	public void beginTextBlock() {
		// TODO Auto-generated method stub
	}

	public void renderText(TextRenderInfo renderInfo) {
		String text = renderInfo.getText();
		if (null != text && text.contains(keyWord)) {
			Float bound = renderInfo.getBaseline().getBoundingRectange();
			ReplaceRegion region = new ReplaceRegion(keyWord);
			region.setH(bound.height == 0 ? defaultH : bound.height);
			region.setW(bound.width);
			region.setX(bound.x);
			region.setY(bound.y - this.fixHeight);
			result.add(region);
		}

	}

	public void endTextBlock() {
		// TODO Auto-generated method stub

	}

	public void renderImage(ImageRenderInfo renderInfo) {
		// TODO Auto-generated method stub

	}
}
