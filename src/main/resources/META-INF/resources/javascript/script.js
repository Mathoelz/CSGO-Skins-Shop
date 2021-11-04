
const LOGGED_COOKIE = "quarkus-credential";

const _URL = "http://localhost:9090";
const weapon_URL = _URL + '/weapon';
const index_URL = weapon_URL + '/index.html';
const profile_URL = _URL + '/user/profile';
const cart_URL = _URL + '/user/cart.html';
const show_URL = profile_URL + '/show.html';



// Quellen: http://fxapps.blogspot.com/2019/12/quarkus-application-with-form.html
// https://www.mediaevent.de/javascript/cookie-read.html
function logOut() {
    //console.log("logging out")
    document.cookie = LOGGED_COOKIE + '=; Max-Age=0; path=/;'
    //console.log(document.cookie)
};

function deleteAccount() {
    const Http = new XMLHttpRequest();

    // get value by id 
    const url = "http://localhost:9090/user/delete";
    // if the function call was successful
    Http.onreadystatechange = function () {
        if (Http.readyState == XMLHttpRequest.DONE) {
            alert("account successfuly deleted");
            document.cookie = LOGGED_COOKIE + '=; Max-Age=0; path=/;'
            parent.location = index_URL;
        }
    }
    // execute post request
    Http.open("DELETE", url);
    Http.send();

};

function addToCart(weapon_id) {
    const Http = new XMLHttpRequest();
    const url = weapon_URL + "/addToCart/" + weapon_id;
    Http.onreadystatechange = function () {
        if (Http.readyState == XMLHttpRequest.DONE) {
            console.log(Http.responseText)
            if (Http.responseText == 0) {
                alert("Weapon was successfully added to your Cart");
            }
            else if (Http.responseText == 1) {
                alert("Weapon is already in your Cart");
            }
            // 0: code is invalid or wrong
            else {
                alert('Please log in first');
            }
        }
    }
    Http.open("PUT", url);
    Http.send();
};

function search() {
    parent.location = weapon_URL + '/' + document.getElementById("txtInput").value + '/search.html';

};

function addFunds(sum) {
    const Http = new XMLHttpRequest();
    Http.onreadystatechange = function () {
        if (Http.readyState == XMLHttpRequest.DONE) {
            alert('Funds added');
            parent.location = show_URL;
        }
    }
    Http.open("POST", profile_URL + "/addFunds?sum=" + sum);
    Http.send();
};

function editProfile() {
    const Http = new XMLHttpRequest();
    const firstName = document.getElementById("firstName").value.replace(" ", "%20");
    const lastName = document.getElementById("lastName").value.replace(" ", "%20");
    const email = document.getElementById("email").value.replace("@", "%40");

    const url = profile_URL + '/edit?email=' + email + '&firstName=' + firstName + '&lastName=' + lastName;
    console.log(url);
    Http.open("PUT", url);
    Http.send();
    parent.location = show_URL;
};

/* -- Cart.html -- */

function removeFromCart(id) {
    const Http = new XMLHttpRequest();
    //console.log(id);
    const url = "http://localhost:9090/weapon/removeFromCart/" + id;
    Http.onreadystatechange = function () {
      if (Http.readyState == XMLHttpRequest.DONE) {
        parent.location = cart_URL
      }
    }
    Http.open("PUT", url);
    Http.send();
  };

function clearCart(id) {
    const Http = new XMLHttpRequest();
    const url = "http://localhost:9090/weapon/clearCart";
    Http.onreadystatechange = function () {
      if (Http.readyState == XMLHttpRequest.DONE) {
        parent.location = cart_URL;
      }
    }
    Http.open("PUT", url);
    Http.send();
  }

  function discount(weapon_id) {
    const Http = new XMLHttpRequest();
    // get value by id 
    const url = "http://localhost:9090/email/discount?weapon_id=" + weapon_id;
    // if the function call was successful
    Http.onreadystatechange = function () {
      if (Http.readyState == XMLHttpRequest.DONE) {
        alert("The owner of the skin was notified");
        parent.location = cart_URL;
      }
    }
    // execute post request
    Http.open("POST", url);
    Http.send();
  };

  function useDiscount() {
    var Http = new XMLHttpRequest();
    var url = "http://localhost:9090/discount/adjustPrice?"
    var discountCode = document.getElementById('discountCode').value;

    var fullURL = url + "DiscountCode=" + discountCode;
    Http.onreadystatechange = function () {
      if (Http.readyState == XMLHttpRequest.DONE) {
        // 1: payment is successfull
        if (Http.responseText == "true") {
          location.reload();
        }
        // 0: code is invalid or wrong
        else {
          alert(`Code invalid or wrong!`);
        }
      }
    }
    Http.open("PUT", fullURL);
    Http.send();
  };

  function completeOrder() {
    const Http = new XMLHttpRequest();
    Http.onreadystatechange = function () {
      if (Http.readyState == XMLHttpRequest.DONE) {
        // 1: payment is successfull
        if (Http.responseText == 1) {
          parent.location = 'http://localhost:9090/success.html'
        }
        // 2: not enough money
        else if (Http.responseText == 2) {
          parent.location = 'http://localhost:9090/failure.html'
        }
        // 0: user cart is empty
        else {
          alert("Your Cart is empty");
        }

      }
    }
    Http.open("PUT", "http://localhost:9090/user/completeOrder");
    Http.send();
  };

  /* -- Sell.html -- */

  function dynamicdropdown(listindex) {
    switch (listindex) {
        case "Consumer grade":
            document.getElementById("rarityColor").innerHTML = "b0c3d9";
            document.getElementById("rarityColor").value = "b0c3d9";
            document.getElementById("rarityColor").style.background = "#b0c3d9";
            break;
        case "Industrial grade":
            document.getElementById("rarityColor").innerHTML = "5e98d9";
            document.getElementById("rarityColor").value = "5e98d9";
            document.getElementById("rarityColor").style.background = "#5e98d9";
            break;
        case "Mil-spec":
            document.getElementById("rarityColor").innerHTML = "4b69ff";
            document.getElementById("rarityColor").value = "4b69ff";
            document.getElementById("rarityColor").style.background = "#4b69ff";
            break;
        case "Restricted":
            document.getElementById("rarityColor").innerHTML = "8847ff";
            document.getElementById("rarityColor").value = "8847ff";
            document.getElementById("rarityColor").style.background = "#8847ff";
            break;
        case "Classified":
            document.getElementById("rarityColor").innerHTML = "d32ce6";
            document.getElementById("rarityColor").value = "d32ce6";
            document.getElementById("rarityColor").style.background = "#d32ce6";
            break;
        case "Covert":
            document.getElementById("rarityColor").innerHTML = "eb4b4b";
            document.getElementById("rarityColor").value = "eb4b4b";
            document.getElementById("rarityColor").style.background = "#eb4b4b";
            break;
        case "Exceedingly Rare":
            document.getElementById("rarityColor").innerHTML = "e26b0a";
            document.getElementById("rarityColor").value = "e26b0a";
            document.getElementById("rarityColor").style.background = "#e26b0a";
            break;
        case "Contraband":
            document.getElementById("rarityColor").innerHTML = "e4ae39";
            document.getElementById("rarityColor").value = "e4ae39";
            document.getElementById("rarityColor").style.background = "#e4ae39";
            break;
    }
    return true;
};

function sellSkin() {
    const Http = new XMLHttpRequest();
    const exterior = document.getElementById("dropdownExterior").value.replace(" ", "%20");
    const gun_type = document.getElementById("inputGunType").value.replace(" ", "%20");
    const icon_url = document.getElementById("inputIconUrl").value.replace(" ", "%20");
    const price = document.getElementById("inputPrice").value.replace(" ", "%20");
    const rarity = document.getElementById("dropdownRarity").value.replace(" ", "%20");
    const rarity_color = document.getElementById("rarityColor").value;
    const weapon_name = document.getElementById("inputWeaponName").value.replace(" ", "%20");
    const weapon_type = document.getElementById("dropdownWeaponType").value.replace(" ", "%20");

    const url = weapon_URL + '/addToSell?exterior=' + exterior + '&gun_type=' + gun_type + '&icon_url=' + icon_url + '&price=' + price + '&rarity=' + rarity + '&rarity_color=' + rarity_color + '&weapon_name=' + weapon_name + '&weapon_type=' + weapon_type;
    Http.onreadystatechange = function () {
      if (Http.readyState == XMLHttpRequest.DONE) {
        parent.location = index_URL
      }
    }
    Http.open("POST", url);
    Http.send();
};

/* -- -- */



