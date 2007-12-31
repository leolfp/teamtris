import javax.media.j3d.*;
import javax.swing.*;
import com.sun.j3d.utils.universe.*;
import java.awt.*;
import com.sun.j3d.utils.geometry.*;
import java.util.*;
import javax.vecmath.*;
import java.awt.event.*;
import com.sun.j3d.loaders.*;
import java.beans.*;
import java.io.*;
import java.util.zip.GZIPInputStream;
import com.sun.j3d.utils.image.TextureLoader;

public class Viewer {

	public static void main(String args[]) {

          if (args.length == 0)
              filename = "Material.000.textured.Mesh.Shape3D.xml.gz";
          else
              filename = args[0];

		GraphicsConfiguration graphicsConfig =
			SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(graphicsConfig);

		JFrame frame = new JFrame();
		JPanel contentPane = (JPanel) frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(canvas, BorderLayout.CENTER);
		frame.setSize(500, 400);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});

		BranchGroup scene = createSceneGraph();

		scene.compile();

		SimpleUniverse simpleU = new SimpleUniverse(canvas);
		View theView = simpleU.getViewer().getView();
		TransformGroup
			viewTransform = simpleU.getViewingPlatform().getViewPlatformTransform();
		Transform3D finalTrans3D;
		Transform3D temp = new Transform3D();
		temp.setTranslation(new Vector3f(0, 0, 2));
		finalTrans3D = temp;
		temp = new Transform3D();
		temp.setTranslation(new Vector3f((float)Math.atan(angleX / viewLength), 0f,
0f));
		finalTrans3D.mul(temp);
		temp = new Transform3D();
		temp.setTranslation(new Vector3f(0f, (float) Math.atan(angleY / viewLength),
0f));
		finalTrans3D.mul(temp);
		temp = new Transform3D();
		//temp.rotZ(Math.PI);
		finalTrans3D.mul(temp);
		viewTransform.setTransform(finalTrans3D);
		simpleU.addBranchGraph(scene);
	}

	static double angleX = 30 / Math.PI * 2;
	static double angleY = 0;
	static double viewLength = 100;
        static String filename;

	static BranchGroup createSceneGraph() {
		BranchGroup sceneRoot = new BranchGroup();

		Light light = new DirectionalLight(new Color3f(Color.WHITE), new Vector3f(1, -1, -1));
		light.setInfluencingBounds(new BoundingSphere());
		sceneRoot.addChild(light);
		light = new AmbientLight();
		light.setInfluencingBounds(new BoundingSphere());
		sceneRoot.addChild(light);

		Transform3D shrink = new Transform3D();
		shrink.setScale(0.15);
		TransformGroup shrinkTransform = new TransformGroup(shrink);

		TransformGroup rotate = new TransformGroup();

		Shape3D s = loadThing(filename);

                Appearance app;
                // textured
                if (textured(filename)) {
                  s.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
                  s.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

                  app = s.getAppearance();
                  app.setCapability(Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE);
                  app.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
                  String texname = (String) s.getUserData();
                  addTexture(texname,app);

                }
                else {

                  // shaded
                  app = new Appearance();
                  PolygonAttributes pa = new PolygonAttributes(PolygonAttributes.
                      POLYGON_LINE,
                      PolygonAttributes.CULL_NONE,
                      0);
                  app.setPolygonAttributes(pa);
                  RenderingAttributes ra = new RenderingAttributes();
                  ra.setIgnoreVertexColors(true);
                  ColoringAttributes ca = new ColoringAttributes(1, 1, 1,
                      ColoringAttributes.NICEST);
                  app.setRenderingAttributes(ra);
                  app.setColoringAttributes(ca);
                  Material mat = new Material();
                  mat.setAmbientColor(0.3f, 0, 0);
                  mat.setLightingEnable(true);
                  app.setMaterial(mat);
                }

		rotate.addChild(s);


		Transform3D t3 = new Transform3D();
		t3.rotZ(Math.PI);
		rotate.setTransform(t3);
		rotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		RotationInterpolator rotator = new RotationInterpolator(new Alpha(-1, 3000),
rotate);
		rotate.addChild(rotator);
		rotator.setSchedulingBounds(new BoundingSphere());

		TransformGroup ribit = new TransformGroup();
		ribit.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Alpha a = new Alpha(-1, 10000);
		a.setMode(Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE);
		a.setIncreasingAlphaDuration(3000);
		a.setDecreasingAlphaDuration(3000);
		ScaleInterpolator scaler = new ScaleInterpolator(a, ribit);

		ribit.addChild(scaler);
		scaler.setSchedulingBounds(new BoundingSphere());
		ribit.addChild(rotate);

		shrinkTransform.addChild(ribit);
		sceneRoot.addChild(shrinkTransform);

		return sceneRoot;

	}

	static Shape3D loadThing(String filename) {
		Shape3D fred = null;
                XMLDecoder de;
		try {

                  if (zipped(filename))
                    de = new XMLDecoder(new BufferedInputStream(new GZIPInputStream(
                        new
                        FileInputStream(
                        filename))));
                  else
                    de = new XMLDecoder(new BufferedInputStream(new
                        FileInputStream(
                        filename)));
                  fred = (Shape3D) de.readObject();
                  de.close();

		} catch(Exception e) {e.printStackTrace();}

		return fred;
	}
        static boolean zipped(String path) {

          int pos = path.lastIndexOf(".gz");
          if (pos > 0) return true;
          else return false;
        }
        static boolean textured(String path) {

          int pos = path.lastIndexOf("textured");
          if (pos > 0) return true;
          else return false;
        }
        static void addTexture(String texname, Appearance app) {

          TextureLoader loader = new TextureLoader(texname, null);
          ImageComponent2D image = loader.getImage();
          Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
                                            image.getWidth(), image.getHeight());
          texture.setImage(0, image);
          texture.setEnable(true);
          app.setTexture(texture);

        }


}
