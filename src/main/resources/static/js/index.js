document.getElementById("open-menu").addEventListener("click", () => {
  document.querySelector(".mobile-nav-links").style.right = "0";
});

document.getElementById("close-menu").addEventListener("click", () => {
  document.querySelector(".mobile-nav-links").style.right = "-20rem";
});

document.querySelectorAll(".mobile-nav-links li").forEach((item) => {
  item.addEventListener("click", () => {
    document.querySelector(".mobile-nav-links").style.right = "-20rem";
  });
});


//$(document).ready(function () {
//    $('#submit-book-btn').on('click', function (e) {
//      e.preventDefault();
//      let form = $('form')[0];
//      let formData = new FormData(form);
//      $.ajax({
//        url: '/my-account/books',
//        type: 'POST',
//        data: formData,
//        processData: false,
//        contentType: false,
//        enctype: 'multipart/form-data',
//        success: function (response) {
//          location.reload();
//        },
//        error: function (xhr) {
//          alert('Error adding book: ' + xhr.responseText);
//        }
//      });
//    });
//  });

$(document).on('click', '.delete-book-btn', function () {
    const bookId = $(this).data('id');

    if (showDeleteConfirm(bookId)) {
        $.ajax({
            url: `/my-account/books/${bookId}`,
            type: 'DELETE',
            success: function () {
                 $(`[data-id="${bookId}"]`).closest('.col-md-4').remove();
            },
            error: function () {
                showToast("Failed to delete book.", "error");
            }
        });
    }
});



// Open Edit Modal
$(document).on('click', '.edit-book-btn', function () {
    const btn = $(this);

    $('#edit-book-id').val(btn.data('id'));
    $('#edit-title').val(btn.data('title'));
    $('#edit-author').val(btn.data('author'));
    $('#edit-genre').val(btn.data('genre'));
    $('#edit-tags').val(btn.data('tags'));
    $('#edit-description').val(btn.data('description'));
    $('#status').val(btn.data('status'));
    $('#edit-imageFile').val(''); // clear file input

    $('#editBookModal').modal('show');
  });
// Submit Edit
$('#edit-book-form').on('submit', function (e) {
  e.preventDefault();

  const bookId = $('#edit-book-id').val();
  const formData = new FormData();
  formData.append('title', $('#edit-title').val());
  formData.append('author', $('#edit-author').val());
  formData.append('genre', $('#edit-genre').val());
  formData.append('tags', $('#edit-tags').val());
  formData.append('description', $('#edit-description').val());
  formData.append('status',$('#status').val());

  const imageFile = $('#edit-imageFile')[0].files[0];
  if (imageFile) {
    formData.append('imageFile', imageFile);
  }

  $.ajax({
    url: '/my-account/books/' + bookId,
    type: 'PUT',
    data: formData,
    processData: false,
    contentType: false,
    success: function () {
      $('#editBookModal').modal('hide');
      setTimeout(() => {
        location.reload(true); // Hard reload
      }, 1000);
    },
    error: function () {
      showToast("Failed to update book.", "error");
    }
  });
});


//profile edit
$(document).on('click', '.edit-profile-btn', function () {
    const btn = $(this);

    $('#edit-profile-id').val(btn.data('id'));
    $('#edit-firstname').val($("#firstName").val());
    $('#edit-lastname').val($("#lastName").val());
    $('#edit-line1').val(btn.data('line1'));
    $('#edit-line2').val(btn.data('line2'));
    $('#edit-mobileno').val(btn.data('mobileno'));
    $('#edit-city').val(btn.data('city'));
    $('#edit-state').val(btn.data('state'));
    $('#edit-postalcode').val($("#postalCode").val()); // clear file input

    $('#editProfileModal').modal('show');
  });

  //submit profile edit

  $('#edit-profile-form').on('submit', function (e) {
    e.preventDefault();


    const userId = $('#edit-profile-id').val();
    const formData1 = new FormData();
    formData1.append('firstName', $('#edit-firstname').val());
    formData1.append('lastName', $('#edit-lastname').val());
     formData1.append('mobileno', $('#edit-mobileno').val())
    formData1.append('line1', $('#edit-line1').val());
    formData1.append('line2', $('#edit-line2').val());
    formData1.append('city', $('#edit-city').val());
    formData1.append('state', $('#edit-state').val());
    formData1.append('postalCode', $('#edit-postalcode').val());

    $.ajax({
        url: '/my-account/profile/' + userId,
        type: 'PUT',
        data: formData1,
        processData: false,
        contentType: false,
        success: function () {
          $('#editProfileModal').modal('hide');
          setTimeout(() => {
            location.reload(true); // Hard reload
          }, 1000);
        },
        error: function () {
          showToast("Failed to update book.", "error");
        }
      });
  });

window.addEventListener('load', function () {
    const loader = document.getElementById('loader-overlay');
    setTimeout(() => {
      loader.style.display = 'none';
    }, 1000); // optional small delay for smoothness

    document.getElementById('country').value = "India";
  });



  function showToast(message, type = 'success') {
    const toastEl = document.getElementById('liveToast');
    const toastBody = document.getElementById('toastBody');

    // Reset background classes
    toastEl.className = 'toast align-items-center border-0';

    // Add Bootstrap background based on type
    switch(type) {
      case 'success':
        toastEl.classList.add('text-bg-success');
        break;
      case 'error':
        toastEl.classList.add('text-bg-danger');
        break;
      case 'warning':
        toastEl.classList.add('text-bg-warning');
        break;
      case 'info':
        toastEl.classList.add('text-bg-info');
        break;
      default:
        toastEl.classList.add('text-bg-secondary');
    }

    // Set message
    toastBody.innerHTML = message;

    // Show toast
    const toast = new bootstrap.Toast(toastEl);
    toast.show();
  }


 function showDeleteConfirm(bookId) {
   const confirmToastEl = document.getElementById('confirmToast');
   const confirmYesBtn = document.getElementById('confirmYesBtn');

   // Re-initialize to remove old events
   const newBtn = confirmYesBtn.cloneNode(true);
   confirmYesBtn.parentNode.replaceChild(newBtn, confirmYesBtn);

   newBtn.addEventListener('click', function () {
     confirmToast.hide();
     $.ajax({
       url: `/my-account/books/${bookId}`,
       type: 'DELETE',
       success: function () {
         $(`[data-id="${bookId}"]`).closest('.col-md-4').remove();
         showToast("✅ Book deleted successfully!", "success");
       },
       error: function () {
         showToast("❌ Failed to delete book.", "error");
       }
     });
   });

   const confirmToast = new bootstrap.Toast(confirmToastEl, {
     autohide: false
   });

   confirmToast.show();
 }


function selectBook(card) {
  // Remove existing selection
  document.querySelectorAll('.book-card').forEach(c => c.classList.remove('selected-book'));

  // Add highlight to selected card
  card.classList.add('selected-book');

  // Store selected book ID
  const bookId = card.getAttribute('data-book-id');
  document.getElementById('selectedBookId').value = bookId;
}







