package aiOps;

import java.io.File;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class OCR {
	protected Tesseract tesseract;
	
	public OCR() {
		this.tesseract = new Tesseract();
		this.tesseract.setDatapath("./src/assets/tesseract/tessdata/");
		this.tesseract.setLanguage("eng");
	}
	
	public float doOCR() {
		float result;
		try {
			result = Float.parseFloat(this.tesseract.doOCR(new File("./src/assets/images/ocrTest2.png")));
		} catch (TesseractException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = 0.0f;
		}
		return result;
	}
}
