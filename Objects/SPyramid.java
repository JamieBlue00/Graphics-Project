package Objects;

public class SPyramid extends SObject{

    public SPyramid() {
        genData();
        update();
    }

    @Override
    protected void genData() {

        // Vertex array is defined with the different positions that triangles can be specified as.
        float[] vertexArray = {
                0, 1, 0,
                -1, -1, 1,
                1, -1, 1,
                1, -1, -1,
                -1, -1, -1,
        };

        // Indices set based off of vertex array index.
        int[] indicesArray = {
                0, 1, 2,
                0, 3, 4,
                0, 4, 1,
                0, 2, 3,
                4, 2, 1,
                4, 3, 2,
        };

        // Normals calculated for pyramid.
        float[] normalsArray = {
                //Triangle - Front
                0, 2, 4,
                0, 2, 4,
                0, 2, 4,

                // Triangle - Right
                0, 2, 0,
                0, 2, 0,
                0, 2, 0,

                //Triangle - Back
                0, 2, -4,
                0, 2, -4,
                0, 2, -4,

                //Triangle - left
                -4, 0, 0,
                -4, 0, 0,
                -4, 0, 0,

                //Triangle - Bottom 1
                0, 4, 0,
                0, 4, 0,
                0, 4, 0,

                //Triangle - Bottom 2
                0, -4, 0,
                0, -4, 0,
                0, -4, 0,
        };



        // Variables from SObject set.
        numVertices = (vertexArray.length/3);
        numIndices = (indicesArray.length);
        vertices = new float[numVertices * 3];
        indices = new int[indicesArray.length];
        normals = new float[numVertices * 3];
        textures = new float[numVertices*2];

        // Loop to add vertices, indices and normals from array to the object variables.
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = vertexArray[i];
            vertices[i] += 3f;
        }
        for (int j = 0; j < indices.length; j++) {
            indices[j] = indicesArray[j];
        }
        for(int q = 0; q < normals.length; q++) {
            normals[q] = normalsArray[q];
        }
    }
}
