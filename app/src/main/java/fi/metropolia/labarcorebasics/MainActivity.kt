package fi.metropolia.labarcorebasics

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.HitTestResult
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var fragment: ArFragment
    private var testRenderable: ModelRenderable? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var button = findViewById<Button>(R.id.button)
        button.setOnClickListener{
            addObject() }

        fragment= supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment;

        // var modelUri = Uri.parse("https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Duck/glTF/Duck.gltf")
         var modelUri = Uri.parse("https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/Box/glTF/Box.gltf")

        val renderableFuture= ModelRenderable.builder()
                .setSource(this, RenderableSource.builder().setSource(
                        this,
                        modelUri,
                        RenderableSource.SourceType.GLTF2)
                        .setScale(0.2f) // Scaletheoriginalmodelto 20%.
                        .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                        .build())
                .setRegistryId("CesiumMan")
                .build()
        renderableFuture.thenAccept{ it -> testRenderable = it }
        renderableFuture.exceptionally{ throwable-> testRenderable}


        /* fragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment;
         val renderableFuture = ViewRenderable.builder()
             .setView(this, R.layout.rendtext)
             .build()
         renderableFuture.thenAccept {it -> testRenderable = it }
         fragment.setOnTapArPlaneListener {hitResult: HitResult?, plane: Plane?, motionEvent: MotionEvent? ->
                     if (testRenderable == null) {
                         return@setOnTapArPlaneListener
                         }
                     val anchor = hitResult!!.createAnchor()
                     val anchorNode = AnchorNode(anchor)
                     anchorNode.setParent(fragment.arSceneView.scene)
                     val viewNode = TransformableNode(fragment.transformationSystem)
                     viewNode.setParent(anchorNode)
                     viewNode.renderable = testRenderable
                     viewNode.select()
                     viewNode.setOnTapListener{hitTestRes: HitTestResult?, motionEv: MotionEvent? ->
                                 Toast.makeText(applicationContext, "Ouch!!!!", Toast.LENGTH_SHORT).show()
                             }
                     } */
    }

    private fun addObject() {
        val frame= fragment.arSceneView.arFrame
        val pt = getScreenCenter()
        val hits: List<HitResult>
        if(frame!= null && testRenderable!= null) {
            hits = frame.hitTest(pt.x.toFloat(), pt.y.toFloat())
            for (hit in hits) {
                val trackable= hit.trackable
                if(trackable is Plane) {
                    val anchor = hit!!.createAnchor()
                    val anchorNode= AnchorNode(anchor)
                    anchorNode.setParent(fragment.arSceneView.scene)
                    val mNode= TransformableNode(fragment.transformationSystem)
                    mNode.setParent(anchorNode)
                    mNode.renderable = testRenderable
                    mNode.select()
                    mNode.setOnTapListener{ hitTestRes: HitTestResult?, motionEv: MotionEvent? ->

                        button.visibility = View.INVISIBLE
                    }
                    break
                } } } }

    private fun getScreenCenter(): android.graphics.Point{
        val vw= findViewById<View>(android.R.id.content)
        return android.graphics.Point(vw.width/ 2, vw.height/ 2)
    }
}