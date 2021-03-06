package be.panako.strategy.chromaprint;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Map;

import be.tarsos.dsp.ui.Axis;
import be.tarsos.dsp.ui.CoordinateSystem;
import be.tarsos.dsp.ui.layers.Layer;

public class ChromaPrintLayer implements Layer, MouseMotionListener {
	
	private final CoordinateSystem cs;
	private final ChromaPrintExtractor fileInfo;
	private Graphics2D graphics;
	
	
	public ChromaPrintLayer(CoordinateSystem cs,ChromaPrintExtractor fileInfo) {
		this.cs = cs;
		this.fileInfo = fileInfo;
	}

	@Override
	public void draw(Graphics2D graphics) {
		this.graphics = graphics;
		float frameDurationInMS = (4096/3)/  ((float) 11025) * 1000.f;
		
		Map<Float, float[]> magnitudesSubMap = fileInfo.chromaMagnitudes.subMap(cs.getMin(Axis.X) / 1000.0f, cs.getMax(Axis.X) / 1000.0f );
				
		for (Map.Entry<Float, float[]> frameEntry : magnitudesSubMap.entrySet()) {
			double timeStart = frameEntry.getKey();// in seconds
			float[] magnitudes = frameEntry.getValue();
		
			// draw the pixels
			for (int i = 0; i < magnitudes.length; i++) {
				Color color = Color.black;
				
				//actual energy at frame.frequencyEstimates[i];
				
				float centsStartingPoint = i * 1000 ;
				// only draw the visible frequency range
				if (centsStartingPoint >= cs.getMin(Axis.Y)
						&& centsStartingPoint <= cs.getMax(Axis.Y)) {
				
					int greyValue = 255 - (int) (magnitudes[i]  * 255);
					greyValue = Math.max(0, greyValue);
					greyValue = Math.min(255, greyValue);
					color = new Color(greyValue, greyValue, greyValue);
					graphics.setColor(color);
					graphics.fillRect((int) Math.round(timeStart * 1000),
							Math.round(centsStartingPoint),
							(int) Math.round(frameDurationInMS),
							(int) Math.ceil(1000));
				}
			}
		}
	}
	
	

	@Override
	public String getName() {		
		return "Chroma Print Audio Info Layer";
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(graphics !=null ){

		}
	}

}
