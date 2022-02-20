package Objects;

public class SCube extends SObject {

    public SCube() {
        super();
        update();
    }

    @Override
    protected void genData() {

        // Vertex array stores the different vertices within the cube.
        int [] vertexArray = {
                1,  1,  1,    // 0
                -1,  1,  1,   // 1
                -1, -1,  1,   // 2
                1, -1,  1,    // 3
                1,  1, -1,    // 4
                -1, -1, -1,   // 5
                -1,  1, -1,   // 6
                1, -1, -1,    // 7

        };

        // Indices array indexes the different points that the cube will be plot at.
        // Each line being a different face of the cube, made of 6 points (2 triangles).
        int[] indicesArray = {
                0, 1, 2, 0, 2, 3,
                4, 5, 6, 4, 7, 5,
                1, 6, 2, 6, 5, 2,
                0, 3, 4, 4, 3, 7,
                0, 4, 1, 1, 4, 6,
                3, 2, 7, 2, 5, 7,
        };

        // Using normal calculation array set including the normal points of the cube.
        float [] normalsArray = {
                // front face
                0, 0, 4,
                0, 0, 4,
                0, 0, 4,
                0, 0, 4,
                // back face
                0, 0, -4,
                0, 0, -4,
                0, 0, -4,
                0, 0, -4,
                // left face
                -4, 0, 0,
                -4, 0, 0,
                -4, 0, 0,
                -4, 0, 0,
                // right face
                4, 0, 0,
                4, 0, 0,
                4, 0, 0,
                4, 0, 0,
                // top face
                0,  4, 0,
                0,  4, 0,
                0,  4, 0,
                0,  4, 0,
                // bottom face
                0,  -4, 0,
                0,  -4, 0,
                0,  -4, 0,
                0,  -4, 0,
        };
        /*
        Array storing texture details
        float[] texturesArray = {
                0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 0,
                0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1,
                0, 0, 2, 1, 0, 2, 1, 1, 2, 0, 1, 2,
                0, 0, 3, 1, 0, 3, 1, 1, 3, 0, 1, 3,
                0, 0, 4, 1, 0, 4, 1, 1, 4, 0, 1, 4,
                0, 0, 5, 1, 0, 5, 1, 1, 5, 0, 1, 5,
        };

         Loop to try and initialise normals, INCOMPLETE
        int a, b, c, d, e, f, g, h, v, uX, uY, uZ, vX, vY, vZ, indicesNo, normalX, normalY, normalZ;
        int normalCount = 0;
        for (int x = 0; x < indicesArray.length; x++) {
            indicesNo = indicesArray[x];
            a = normalsArray2[3 * indicesNo];
            b = normalsArray2[3 * indicesNo + 1];
            c = normalsArray2[3 * indicesNo + 2];
            d = normalsArray2[3 * indicesNo + 3];
            e = normalsArray2[3 * indicesNo + 4];
            f = normalsArray2[3 * indicesNo + 5];
            g = normalsArray2[3 * indicesNo + 6];
            h = normalsArray2[3 * indicesNo + 7];
            v = normalsArray2[3 * indicesNo + 8];

            uX = d - a;
            uY = e - b;
            uZ = f - c;

            vX = g - a;
            vY = h - b;
            vZ = v - c;

            normalX = (uY * vZ) - (uZ * vY);
            normalY = (uZ * vX) - (uX * vZ);
            normalZ = (uX * vY) - (uY * vX);

            normalsArray[normalCount] = normalX;
            normalsArray[normalCount + 1] = normalY;
            normalsArray[normalCount + 2] = normalZ;

            normalCount += 3;
            x += 6;
        }
        */

        numVertices = (vertexArray.length/3);
        numIndices = (indicesArray.length);
        vertices = new float[numVertices * 3];
        indices = new int[numIndices];
        normals = new float[numVertices * 3];
        textures = new float[numVertices * 2];


        // Loop to add vertices, indices and normals from array to the object variables.
        for(int i = 0; i < vertices.length; i++) {
            vertices[i] = vertexArray[i];
            vertices[i] -= 5f;
        }

        for (int j = 0; j < indices.length; j++) {
            indices[j] = indicesArray[j];
        }
        for(int q = 0; q < normals.length; q++) {
            normals[q] = normalsArray[q];
        }


        // NOT USED setting texture array list.
        /*
        for(int x = 0; x < texturesArray.length; x++) {
            textures[x] = texturesArray[x];
        }

         */

    }



}