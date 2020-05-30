<?php

     include 'config.inc.php';

         // Check whether username or password is set from android      
     if(isset($_POST['id_classe']))
     {
                  // Innitialize Variable
                  $result='';
                  $ID = $_POST['id_classe'];

                  // Query database for row exist or not
          $sql = 'SELECT nom, prenom FROM Etudiant  WHERE  id_classe = :ID';
          $stmt = $conn->prepare($sql);
          $stmt->bindParam(':ID', $ID, PDO::PARAM_STR);
          $stmt->execute();

          if($stmt->rowCount())
          {
                         $output = $stmt->fetchAll(PDO::FETCH_ASSOC);
			  echo(json_encode($output));
          
          }  
          elseif(!$stmt->rowCount())
          {
                              $result="false";
			      echo $result;

          }

                  // send result back to android

        }

?>

