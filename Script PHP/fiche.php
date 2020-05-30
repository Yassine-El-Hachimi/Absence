<?php

     include 'config.inc.php';

         // Check whether username or password is set from android      
     if(isset($_POST['id_fiche']) && isset($_POST['id_classe']) && isset($_POST['cours'])&&isset($_POST['fiche']))
     {
                  // Innitialize Variable
                  $result='';
                  $id_fiche = $_POST['id_fiche'];
                  $id_classe = $_POST['id_classe'];
		  $cours = $_POST['cours'];
		  $fiche = $_POST['fiche'];
                  // Query database for row exist or not
          $sql = 'INSERT INTO FicheAbsence VALUES(:id_fiche,:id_classe,:cours,:fiche)';
          $stmt = $conn->prepare($sql);
          $stmt->bindParam(':id_fiche', $id_fiche, PDO::PARAM_STR);
          $stmt->bindParam(':id_classe', $id_classe, PDO::PARAM_STR);
	  $stmt->bindParam(':cours', $cours, PDO::PARAM_STR);
	  $stmt->bindParam(':fiche', $fiche, PDO::PARAM_STR);
          $stmt->execute();

       }   

?>

