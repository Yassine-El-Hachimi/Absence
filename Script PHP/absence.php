<?php

     include 'config.inc.php';

         // Check whether username or password is set from android      
     if(isset($_POST['cours']))
     {
                  // Innitialize Variable
                  $result='';
                  $cours = $_POST['cours'];

                  // Query database for row exist or not
          $sql = 'SELECT fiche FROM FicheAbsence  WHERE cours = :cours';
          $stmt = $conn->prepare($sql);
          $stmt->bindParam(':cours', $cours, PDO::PARAM_STR);
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
