<?php

     include 'config.inc.php';

         // Check whether username or password is set from android      
     if(isset($_POST['ID']) && isset($_POST['mdp']))
     {
                  // Innitialize Variable
                  $result='';
                  $ID = $_POST['ID'];
                  $mdp = $_POST['mdp'];

                  // Query database for row exist or not
          $sql = 'SELECT nom, prenom,nomCours, id_classe FROM Enseignant as e, Cours as C  WHERE e.Id_prof = C.id_prof and  e.Id_prof = :ID AND e.mdp = :mdp';
          $stmt = $conn->prepare($sql);
          $stmt->bindParam(':ID', $ID, PDO::PARAM_STR);
          $stmt->bindParam(':mdp', $mdp, PDO::PARAM_STR);
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

