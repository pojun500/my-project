package com.example.xdocreport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.itext.extension.font.AbstractFontRegistry;
import fr.opensagres.xdocreport.itext.extension.font.IFontProvider;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {

		long startTime = System.currentTimeMillis();

		try {
			// 1) Load docx with POI XWPFDocument
			InputStream in = new FileInputStream("d:\\合同协议书.docx");
			XWPFDocument document = new XWPFDocument(in);

			// 2) Convert POI XWPFDocument 2 PDF with iText
			File outFile = new File("d:\\合同协议书.pdf");
			outFile.getParentFile().mkdirs();

			OutputStream out = new FileOutputStream(outFile);
			PdfOptions options = PdfOptions.create();//.fontEncoding("windows-1250");
			//IFontProvider fontProvider = new AbstractFontRegistry();
			//options.fontProvider(fontProvider);
			PdfConverter.getInstance().convert(document, out, options);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		System.out.println("Generate DocxBig.pdf with " + (System.currentTimeMillis() - startTime) + " ms.");

	}
}
