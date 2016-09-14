<script language="JavaScript">
  function img_on(name) {
    if (document.images) 
      document['img_'+name].src = eval('img_'+name+'_on.src');
  }

  function img_off(name) {
    if (document.images)
      document['img_'+name].src = eval('img_'+name+'_off.src');
  }
</script>


<script language="JavaScript">

<!--
if (document.images) {

  img_help_off = new Image();
  img_help_off.src = 'help.gif';
  img_help_on = new Image();
  img_help_on.src = 'helpHL.gif';

  img_signin_off = new Image();
  img_signin_off.src = 'sign-in.gif';
  img_signin_on = new Image();
  img_signin_on.src = 'sign-inHL.gif';
  
  img_signout_off = new Image();
  img_signout_off.src = 'sign-out.gif';
  img_signout_on = new Image();
  img_signout_on.src = 'sign-outHL.gif';

  img_search_off = new Image();
  img_search_off.src = 'search.gif';
  img_search_on = new Image();
  img_search_on.src = 'searchHL.gif';

  img_myaccount_off = new Image();
  img_myaccount_off.src = 'my_account.gif';
  img_myaccount_on = new Image();
  img_myaccount_on.src = 'my_accountHL.gif';

  img_cart_off = new Image();
  img_cart_off.src = 'cart.gif';
  img_cart_on = new Image();
  img_cart_on.src = 'cartHL.gif';

  img_fish_off = new Image();
  img_fish_off.src = 'fish.gif';
  img_fish_on = new Image();
  img_fish_on.src = 'fishHL.gif';
  
  img_dogs_off = new Image();
  img_dogs_off.src = 'dogs.gif';
  img_dogs_on = new Image();
  img_dogs_on.src = 'dogsHL.gif';
  
  img_reptiles_off = new Image();
  img_reptiles_off.src = 'reptiles.gif';
  img_reptiles_on = new Image();
  img_reptiles_on.src = 'reptilesHL.gif';
  
  img_cats_off = new Image();
  img_cats_off.src = 'cats.gif';
  img_cats_on = new Image();
  img_cats_on.src = 'catsHL.gif';
  
  img_birds_off = new Image();
  img_birds_off.src = 'birds.gif';
  img_birds_on = new Image();
  img_birds_on.src = 'birdsHL.gif';
}

// -->
</script>