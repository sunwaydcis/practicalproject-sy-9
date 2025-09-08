package ch.makery.address

import ch.makery.address.model.Person
import ch.makery.address.util.Database
import javafx.fxml.FXMLLoader
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes.*
import javafx.scene as jfxs
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import ch.makery.address.view.{PersonEditDialogController, PersonOverviewController}
import scalafx.scene.image.Image
import scalafx.stage.{Modality, Stage}

object MainApp extends JFXApp3:
  Database.setupDB()
  //Window Root Pane
  var roots: Option[scalafx.scene.layout.BorderPane] = None

  var cssResource = getClass.getResource("view/DarkTheme.css")

  var personOverviewController: Option[PersonOverviewController] = None

  override def start(): Unit =
    // transform path of RootLayout.fxml to URI for resource location.
    val rootResource = getClass.getResource("view/RootLayout.fxml")
    // initialize the loader object.
    val loader = new FXMLLoader(rootResource)
    // Load root layout from fxml file.
    loader.load()

    // retrieve the root component BorderPane from the FXML
    roots = Option(loader.getRoot[jfxs.layout.BorderPane])

    stage = new PrimaryStage():
      title = "AddressApp"
      icons += new Image(getClass.getResource("/images/book.png").toExternalForm)
      scene = new Scene():
        root = roots.get
        stylesheets = Seq(cssResource.toExternalForm)

    // call to display PersonOverview when app start
    showPersonOverview()
  // actions for display person overview window
  def showPersonOverview(): Unit =
    val resource = getClass.getResource("view/PersonOverview.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    val ctrl = loader.getController[PersonOverviewController]
    personOverviewController = Option(ctrl)
    this.roots.get.center = roots

  /**
   * The data as an observable list of Persons.
   */
  val personData = new ObservableBuffer[Person]()

  /**
   * Constructor
   */
  personData ++= Person.getAllPersons

  def showPersonEditDialog(person: Person): Boolean =
    val resource = getClass.getResource("view/PersonEditDialog.fxml")
    val loader = new FXMLLoader(resource)
    loader.load();
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[PersonEditDialogController]
  
    val dialog = new Stage():
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene:
        root = roots2
        stylesheets = Seq(cssResource.toExternalForm)
  
    control.dialogStage = dialog
    control.person = person
    dialog.showAndWait()
    control.okClicked


//
//  // stringProperty
//  val stringA = new StringProperty("hello") //publisher
//  val stringB = new StringProperty("sunway") //subscriber
//  val stringC = new StringProperty("sunway") //subscriber
//
////  stringA.value = "world"
////  stringB <==> stringA
////  stringC <== stringA
////
////  println(stringB.value)
//
////  val func = (a: Int) =>
////    a + 1
////
////  println(func1(8))
//
//
//  stringA.onChange {(_, oldValue, newValue) =>
//    println(s"stringA changed from $oldValue to $newValue")
//  }
//
//  stringA.value = "world"
