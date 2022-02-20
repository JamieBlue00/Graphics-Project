import Basic.ShaderProg;
import Basic.Transform;
import Basic.Vec4;
import Objects.SCube;
import Objects.SObject;
import Objects.SPyramid;
import Objects.SSphere;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;

import javax.swing.*;
import java.awt.event.*;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static com.jogamp.opengl.GL3.*;

public class Coursework extends JFrame{

    final GLCanvas canvas; //Define a canvas
    final FPSAnimator animator=new FPSAnimator(60, true);
    final Renderer renderer = new Renderer();

    public Coursework() {
        GLProfile glp = GLProfile.get(GLProfile.GL3);
        GLCapabilities caps = new GLCapabilities(glp);
        canvas = new GLCanvas(caps);

        add(canvas, java.awt.BorderLayout.CENTER); // Put the canvas in the frame
        canvas.addGLEventListener(renderer); //Set the canvas to listen GLEvents
        canvas.addMouseListener(renderer);
        canvas.addMouseMotionListener(renderer);
        canvas.addKeyListener(renderer);
        animator.add(canvas);

        setTitle("Coursework");
        setSize(500,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        animator.start();
        canvas.requestFocus();
    }

    public static void main(String[] args) {
        new Coursework();

    }

    class Renderer implements GLEventListener, MouseListener, MouseMotionListener, KeyListener {
        private Texture texture;
        private Transform T = new Transform();

        //VAOs and VBOs parameters
        private int idPoint=0, numVAOs = 3;
        private int idBuffer=0, numVBOs = 3;
        private int idElement=0, numEBOs = 3;
        private int[] VAOs = new int[numVAOs];
        private int[] VBOs = new int[numVBOs];
        private int[] EBOs = new int[numEBOs];
        private int[] VBOs2 = new int[numVBOs];

        //Model parameters
        private int numElements;
        private int numElements2;
        private int numElements3;
        private int vPosition;
        private int vNormal;
        private int vPosition2;
        private int vNormal2;
        private int vPosition3;
        private int vNormal3;

        //Transformation parameters
        private int ModelView;
        private int NormalTransform;
        private int Projection;
        private int ModelView2;
        private int NormalTransform2;
        private int Projection2;
        private int ModelView3;
        private int NormalTransform3;
        private int Projection3;
        private float scale = 1;
        private float tx = 0;
        private float ty = 0;
        private float rx = 0;
        private float ry = 0;

        //texture parameters
        ByteBuffer texImg;
        private int texWidth, texHeight;

        //Mouse position
        private int xMouse = 0;
        private int yMouse = 0;

        @Override
        public void display(GLAutoDrawable drawable) {
            GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this
            GL3 gl2 = drawable.getGL().getGL3(); // Get the GL pipeline object this

            gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            gl2.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

//			gl.glBindVertexArray(VAOs[idPoint]);

            T.initialize();
            T.scale(0.3f, 0.3f, 0.3f);
            T.translate(0.0f, 0.0f, 0.0f);

            //Mouse control interactive
            T.scale(scale, scale, scale);
            T.rotateX(rx);
            T.rotateY(ry);
            T.translate(tx, ty, 0);

            //Locate camera
//			T.LookAt(0, 0, 0, 0, 0, -1, 0, 1, 0);  	//Default

            //Send model_view and normal transformation matrices to shader.
            //Here parameter 'true' for transpose means to convert the row-major
            //matrix to column major one, which is required when vertices'
            //location vectors are pre-multiplied by the model_view matrix.
            //Note that the normal transformation matrix is the inverse-transpose
            //matrix of the vertex transformation matrix

            gl.glUniformMatrix4fv(ModelView, 1, true, T.getTransformv(), 0);
            gl.glUniformMatrix4fv(NormalTransform, 1, true, T.getInvTransformTv(), 0);

            gl.glBindVertexArray(VAOs[0]);
            gl.glDrawElements(GL_TRIANGLES, numElements, GL_UNSIGNED_INT, 0);

            gl2.glUniformMatrix4fv(ModelView2, 1, true, T.getTransformv(), 1);
            gl2.glUniformMatrix4fv(NormalTransform2, 1, true, T.getInvTransformTv(), 1);

            gl2.glBindVertexArray(VAOs[1]);
            gl2.glDrawElements(GL_TRIANGLES, numElements, GL_UNSIGNED_INT, 0);

            gl.glUniformMatrix4fv( ModelView3, 1, true, T.getTransformv(), 2 );
            gl.glUniformMatrix4fv( NormalTransform3, 1, true, T.getInvTransformTv(), 2 );

            gl.glBindVertexArray(VAOs[2]);
            gl.glDrawElements(GL_TRIANGLES, numElements, GL_UNSIGNED_INT, 0);

        }

        @Override
        public void dispose(GLAutoDrawable drawable) {
            // TODO Auto-generated method stub
        }

        @Override
        public void init(GLAutoDrawable drawable) {
            GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this

            /*
            try {
                texture = TextureIO.newTexture(new File("WelshDragon.jpg"), false);
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }

             */

            SObject object = new SSphere(2);
            float [] vertexArray = object.getVertices();
            float [] normalArray = object.getNormals();
            int [] vertexIndexs = object.getIndices();
            numElements = object.getNumIndices();
            //float [] textureArray = object.getTextures();

            gl.glGenVertexArrays(numVAOs,VAOs,0);
            gl.glBindVertexArray(VAOs[idPoint]);

            FloatBuffer vertices = FloatBuffer.wrap(vertexArray);
            FloatBuffer normals = FloatBuffer.wrap(normalArray);
            //FloatBuffer textures = FloatBuffer.wrap(textureArray);

            gl.glGenBuffers(numVBOs, VBOs,0);
            gl.glBindBuffer(GL_ARRAY_BUFFER, VBOs[idBuffer]);

            // Create an empty buffer with the size we need
            // and a null pointer for the data values
            long vertexSize = vertexArray.length*(Float.SIZE/8);
            long normalSize = normalArray.length*(Float.SIZE/8);
            //long texSize = textureArray.length*(Float.SIZE/8);
            gl.glBufferData(GL_ARRAY_BUFFER, vertexSize +normalSize,
                    null, GL_STATIC_DRAW); // pay attention to *Float.SIZE/8

            // Load the real data separately.
            gl.glBufferSubData( GL_ARRAY_BUFFER, 0, vertexSize, vertices );
            gl.glBufferSubData( GL_ARRAY_BUFFER, vertexSize, normalSize, normals );
            //gl.glBufferSubData( GL_ARRAY_BUFFER, vertexSize + normalSize, texSize, textures );

            IntBuffer elements = IntBuffer.wrap(vertexIndexs);

            gl.glGenBuffers(numEBOs, EBOs,0);
            gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBOs[idElement]);

            long indexSize = vertexIndexs.length*(Integer.SIZE/8);
            gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexSize,
                    elements, GL_DYNAMIC_DRAW); // pay attention to *Float.SIZE/8

            // Shader is loaded and initialised under int program.
            ShaderProg shaderproc = new ShaderProg(gl, "Gouraud.vert", "Gouraud.frag");
            int program = shaderproc.getProgram();
            gl.glUseProgram(program);

            // Initialize the vertex position attribute in the vertex shader
            vPosition = gl.glGetAttribLocation( program, "vPosition" );
            gl.glEnableVertexAttribArray(vPosition);
            gl.glVertexAttribPointer(vPosition, 3, GL_FLOAT, false, 0, 0L);

            int vNormal = gl.glGetAttribLocation( program, "vNormal" );
            gl.glEnableVertexAttribArray(vNormal);
            gl.glVertexAttribPointer(vNormal, 3, GL_FLOAT, false, 0, vertexSize);


            //Get connected with the ModelView matrix in the vertex shader
            ModelView = gl.glGetUniformLocation(program, "ModelView");
            NormalTransform = gl.glGetUniformLocation(program, "NormalTransform");
            Projection = gl.glGetUniformLocation(program, "Projection");


            // NOT USED, setting the texture to uniform array.
            //gl.glUniform1i( gl.glGetUniformLocation(program, "tex"), 0 );



            // Initialize shader lighting parameters
            float[] lightPosition = {1.0f, 1.0f, 1.0f, 1.0f};
            Vec4 lightAmbient = new Vec4(2.0f, 2.0f, 2.0f, 2.0f);
            Vec4 lightDiffuse = new Vec4(2.0f, 2.0f, 2.0f, 2.0f);
            Vec4 lightSpecular = new Vec4(2.0f, 2.0f, 2.0f, 2.0f);


            //Brass material
            Vec4 materialAmbient = new Vec4(0.1f, 0.18725f, 0.1745f, 0.8f);
            Vec4 materialDiffuse = new Vec4(0.396f, 0.74151f, 0.69102f, 0.8f);
            Vec4 materialSpecular = new Vec4(0.297254f, 0.30829f, 0.306678f, 0.8f);
            float  materialShininess = 12.8f;

            Vec4 ambientProduct = lightAmbient.times(materialAmbient);
            float[] ambient = ambientProduct.getVector();
            Vec4 diffuseProduct = lightDiffuse.times(materialDiffuse);
            float[] diffuse = diffuseProduct.getVector();
            Vec4 specularProduct = lightSpecular.times(materialSpecular);
            float[] specular = specularProduct.getVector();



            gl.glUniform4fv( gl.glGetUniformLocation(program, "AmbientProduct"),
                    0, ambient,1 );
            gl.glUniform4fv( gl.glGetUniformLocation(program, "DiffuseProduct"),
                    0, diffuse, 1 );
            gl.glUniform4fv( gl.glGetUniformLocation(program, "SpecularProduct"),
                    0, specular, 1 );

            gl.glUniform4fv( gl.glGetUniformLocation(program, "LightPosition"),
                    0, lightPosition, 1 );

            gl.glUniform1f( gl.glGetUniformLocation(program, "Shininess"),
                    materialShininess );

            gl.glBindVertexArray(VAOs[idPoint+1]);


            SObject object2 = new SCube();
            float [] vertexArray2 = object2.getVertices();
            float [] normalArray2 = object2.getNormals();
            int [] vertexIndexs2 = object2.getIndices();
            numElements += object2.getNumIndices();

            gl.glGenVertexArrays(1,VAOs,1);
            gl.glBindVertexArray(VAOs[idPoint+1]);

            FloatBuffer vertices2 = FloatBuffer.wrap(vertexArray2);
            FloatBuffer normals2 = FloatBuffer.wrap(normalArray2);

            gl.glGenBuffers(1, VBOs,1);
            gl.glBindBuffer(GL_ARRAY_BUFFER, VBOs[idBuffer+1]);

            // Create an empty buffer with the size we need
            // and a null pointer for the data values
            long vertexSize2 = vertexArray2.length*(Float.SIZE/8);
            long normalSize2 = normalArray2.length*(Float.SIZE/8);

            gl.glBindVertexArray(VAOs[idPoint+1]);

            gl.glBufferData(GL_ARRAY_BUFFER, vertexSize2 +normalSize2,
                    null, GL_STATIC_DRAW); // pay attention to *Float.SIZE/8

            // Load the real data separately.
            gl.glBufferSubData( GL_ARRAY_BUFFER, 0, vertexSize2, vertices2 );
            gl.glBufferSubData( GL_ARRAY_BUFFER, vertexSize2, normalSize2, normals2 );

            IntBuffer elements2 = IntBuffer.wrap(vertexIndexs2);

            gl.glGenBuffers(2, EBOs,idElement+1);
            gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBOs[1]);


            long indexSize2 = vertexIndexs2.length*(Integer.SIZE/8);
            gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexSize2,
                    elements2, GL_STATIC_DRAW); // pay attention to *Float.SIZE/8
            //Brass material

            ShaderProg shaderproc2 = new ShaderProg(gl, "Gouraud.vert", "Gouraud.frag");
            int program2 = shaderproc2.getProgram();
            gl.glUseProgram(program2);

            // Initialize the vertex position attribute in the vertex shader
            vPosition = gl.glGetAttribLocation( program2, "vPosition" );
            gl.glEnableVertexAttribArray(vPosition);
            gl.glVertexAttribPointer(vPosition, 3, GL_FLOAT, false, 0, 0L);

            // Initialize the vertex color attribute in the vertex shader.
            // The offset is the same as in the glBufferSubData, i.e., vertexSize
            // It is the starting point of the color data
            vNormal = gl.glGetAttribLocation( program2, "vNormal" );
            gl.glEnableVertexAttribArray(vNormal);
            gl.glVertexAttribPointer(vNormal, 3, GL_FLOAT, false, 0, vertexSize2);

            //Get connected with the ModelView matrix in the vertex shader
            ModelView = gl.glGetUniformLocation(program2, "ModelView");
            NormalTransform = gl.glGetUniformLocation(program2, "NormalTransform");
            Projection = gl.glGetUniformLocation(program2, "Projection");


            Vec4 materialAmbient2 = new Vec4(0.25f, 0.25f, 0.25f, 1f);
            Vec4 materialDiffuse2 = new Vec4(0.4f, 0.4f, 0.4f, 1f);
            Vec4 materialSpecular2 = new Vec4(0.774597f, 0.774597f, 0.774597f, 1f);
            float  materialShininess2 = 76.8f;

            Vec4 ambientProduct2 = lightAmbient.times(materialAmbient2);
            float[] ambient2 = ambientProduct2.getVector();
            Vec4 diffuseProduct2 = lightDiffuse.times(materialDiffuse2);
            float[] diffuse2 = diffuseProduct2.getVector();
            Vec4 specularProduct2 = lightSpecular.times(materialSpecular2);
            float[] specular2 = specularProduct2.getVector();

            gl.glUniform4fv( gl.glGetUniformLocation(program2, "AmbientProduct"),
                    1, ambient2,0 );
            gl.glUniform4fv( gl.glGetUniformLocation(program2, "DiffuseProduct"),
                    1, diffuse2, 0 );
            gl.glUniform4fv( gl.glGetUniformLocation(program2, "SpecularProduct"),
                    1, specular2, 0 );

            gl.glUniform4fv( gl.glGetUniformLocation(program2, "LightPosition"),
                    1, lightPosition, 0 );

            gl.glUniform1f( gl.glGetUniformLocation(program2, "Shininess"),
                    materialShininess2 );




            gl.glBindVertexArray(VAOs[2]);


            SObject object3 = new SPyramid();
            float [] vertexArray3 = object3.getVertices();
            float [] normalArray3 = object3.getNormals();
            int [] vertexIndexs3 = object3.getIndices();
            numElements += object3.getNumIndices();

            gl.glGenVertexArrays(1,VAOs,2);
            gl.glBindVertexArray(VAOs[2]);

            FloatBuffer vertices3 = FloatBuffer.wrap(vertexArray3);
            FloatBuffer normals3 = FloatBuffer.wrap(normalArray3);

            gl.glGenBuffers(1, VBOs,2);
            gl.glBindBuffer(GL_ARRAY_BUFFER, VBOs[2]);

            // Create an empty buffer with the size we need
            // and a null pointer for the data values
            long vertexSize3 = vertexArray3.length*(Float.SIZE/8);
            long normalSize3 = normalArray3.length*(Float.SIZE/8);

            gl.glBindVertexArray(VAOs[2]);

            gl.glBufferData(GL_ARRAY_BUFFER, vertexSize3 +normalSize3,
                    null, GL_STATIC_DRAW); // pay attention to *Float.SIZE/8

            // Load the real data separately.
            gl.glBufferSubData( GL_ARRAY_BUFFER, 0, vertexSize3, vertices3 );
            gl.glBufferSubData( GL_ARRAY_BUFFER, vertexSize3, normalSize3, normals3 );

            IntBuffer elements3 = IntBuffer.wrap(vertexIndexs3);

            gl.glGenBuffers(1, EBOs,2);
            gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBOs[2]);


            long indexSize3 = vertexIndexs3.length*(Integer.SIZE/8);
            gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexSize3,
                    elements3, GL_STATIC_DRAW); // pay attention to *Float.SIZE/8

            ShaderProg shaderproc3 = new ShaderProg(gl, "Gouraud.vert", "Gouraud.frag");
            int program3 = shaderproc3.getProgram();
            gl.glUseProgram(program3);

            // Initialize the vertex position attribute in the vertex shader
            vPosition3 = gl.glGetAttribLocation( program3, "vPosition" );
            gl.glEnableVertexAttribArray(vPosition3);
            gl.glVertexAttribPointer(vPosition3, 3, GL_FLOAT, false, 0, 0L);

            // Initialize the vertex color attribute in the vertex shader.
            // The offset is the same as in the glBufferSubData, i.e., vertexSize
            // It is the starting point of the color data
            vNormal = gl.glGetAttribLocation( program3, "vNormal" );
            gl.glEnableVertexAttribArray(vNormal);
            gl.glVertexAttribPointer(vNormal, 3, GL_FLOAT, false, 0, vertexSize3);

            //Get connected with the ModelView matrix in the vertex shader
            ModelView = gl.glGetUniformLocation(program3, "ModelView");
            NormalTransform = gl.glGetUniformLocation(program3, "NormalTransform");
            Projection = gl.glGetUniformLocation(program3, "Projection");

            //Brass material
            Vec4 materialAmbient3 = new Vec4(0.1f, 0.18725f, 0.1745f, 0.8f);
            Vec4 materialDiffuse3 = new Vec4(0.396f, 0.74151f, 0.69102f, 0.8f);
            Vec4 materialSpecular3 = new Vec4(0.297254f, 0.30829f, 0.306678f, 0.8f);
            float  materialShininess3 = 12.8f;

            Vec4 ambientProduct3 = lightAmbient.times(materialAmbient3);
            float[] ambient3 = ambientProduct3.getVector();
            Vec4 diffuseProduct3 = lightDiffuse.times(materialDiffuse3);
            float[] diffuse3 = diffuseProduct3.getVector();
            Vec4 specularProduct3 = lightSpecular.times(materialSpecular3);
            float[] specular3 = specularProduct3.getVector();

            gl.glUniform4fv( gl.glGetUniformLocation(program3, "AmbientProduct"),
                    1, ambient3,0 );
            gl.glUniform4fv( gl.glGetUniformLocation(program3, "DiffuseProduct"),
                    1, diffuse3, 0 );
            gl.glUniform4fv( gl.glGetUniformLocation(program3, "SpecularProduct"),
                    1, specular3, 0 );

            gl.glUniform4fv( gl.glGetUniformLocation(program3, "LightPosition"),
                    1, lightPosition, 0 );

            gl.glUniform1f( gl.glGetUniformLocation(program3, "Shininess"),
                    materialShininess3 );



            // This is necessary. Otherwise, the The color on back face may display
//		    gl.glDepthFunc(GL_LESS);
            gl.glEnable(GL_DEPTH_TEST);

        }

        @Override
        public void reshape(GLAutoDrawable drawable, int x, int y, int w,
                            int h) {

            GL3 gl = drawable.getGL().getGL3(); // Get the GL pipeline object this

            gl.glViewport(x, y, w, h);

            T.initialize();

            GL3 gl2 = drawable.getGL().getGL3(); // Get the GL pipeline object this

            gl2.glViewport(x, y, w, h);

            T.initialize();

            //projection
//			T.Ortho(-1, 1, -1, 1, -1, 1);  //Default
            // to avoid shape distortion because of reshaping the viewport
            //viewport aspect should be the same as the projection aspect
            if(h<1){h=1;}
            if(w<1){w=1;}
            float a = (float) w/ h;   //aspect
            if (w < h) {
                T.ortho(-1, 1, -1/a, 1/a, -1, 1);
            }
            else{
                T.ortho(-1*a, 1*a, -1, 1, -1, 1);
            }

            // Convert right-hand to left-hand coordinate system
            T.reverseZ();
            gl.glUniformMatrix4fv( Projection, 1, true, T.getTransformv(), 0 );
            gl2.glUniformMatrix4fv( Projection, 1, true, T.getTransformv(), 0 );

        }
        @Override
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            //left button down, move the object
            if((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0){
                // Fill in code here, so that the object moves
                // in the same direction as the mouse motion.

                ;

                tx += (x-xMouse) * 0.001;
                ty -= (y-yMouse) * 0.001;
                xMouse = x;
                yMouse = y;

            }

            //right button down, rotate the object
            if((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0){
                // To mimic using the mouse to rotate the object, we want that
                // when the mouse moves in horizontal (x) direction,
                // the object will rotate around the vertical (y) axis
                ry += (x-xMouse); // the rotation angle around the y axis

                // Add code here to calculate the rotation angle around the x axis
                rx += (y-yMouse);
                xMouse = x;
                yMouse = y;

            }

            //middle button down, scale the object
            if((e.getModifiers() & InputEvent.BUTTON2_MASK) != 0){
                //Add code here so that the object will scale down (shrink)
                // when the mouse moves up (y increases),
                // and it will scale up (expand) when the mouse moves down.
                scale *= Math.pow(1.1, (y-yMouse) * 0.5);
                xMouse = x;
                yMouse = y;
            }
        }
        @Override
        public void mouseMoved(MouseEvent e) {
            xMouse = e.getX();
            yMouse = e.getY();
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub

        }
        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Auto-generated method stub

        }
        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub

        }
        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub

        }
        @Override
        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub
            boolean keys[] = new boolean[KeyEvent.KEY_TYPED];
            keys[e.getKeyCode()] = true;

            if (keys[KeyEvent.VK_RIGHT]) {
                tx += 0.005;
            } else if (keys[KeyEvent.VK_LEFT]) {
                tx -= 0.005;
            } else if (keys[KeyEvent.VK_UP]) {
                ty += 0.005;
            } else if (keys[KeyEvent.VK_DOWN]) {
                ty -= 0.005;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }
}