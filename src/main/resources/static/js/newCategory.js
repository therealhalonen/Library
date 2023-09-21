// Define the handleCategoryChange function
function handleCategoryChange(selectElement) {
    console.log("handleCategoryChange RAN");
    var selectedValue = selectElement.value;

    // Get a reference to the "category-form" element
    var categoryForm = document.getElementById("category-form");

    if (selectedValue === "0") {
        // Show the category form
        categoryForm.style.display = "block";
    } else {
        // Keep the category form hidden
        categoryForm.style.display = "none";
    }
}

// Wrap c ode inside a DOMContentLoaded event listener
document.addEventListener("DOMContentLoaded", function () {
    // Log a message to confirm that the script is loaded and executed, for testing purposes
    console.log("Script loaded and executed");

    // Get the "category" select element and its currently selected option
    var selectElement = document.getElementById("category");
    var optionElement = selectElement.options[selectElement.selectedIndex];
    var value = optionElement.value;

    // Get a reference to the "category-form" element
    var categoryForm = document.getElementById("category-form");

    // Function to reload the page
    function reloadPage() {
        window.location.reload();
    }

    // Get the CSRF token from the meta tags
    var csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
    var csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

    // Function to submit the new category
    function submitCategoryForm(event) {
        console.log("KUKKUU"); // See if the scripts run here, for testing purposes
        event.preventDefault(); // Prevent the default form submission

        // Get the category data from the input field
        var categoryData = {
            name: document.querySelector("#name").value
        };

        // Send a POST request with the category data to '/savecategory' including the CSRF token
        fetch('/savecategory', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                // Include the CSRF token in the headers
                [csrfHeader]: csrfToken
            },
            body: JSON.stringify(categoryData)
        })
            .then(response => response.json())
            .then(data => {
                // Handle the response data
                console.log(data);

                if (data.success) {
                    // Update the selection form with the new category
                    var selectElement = document.getElementById("category");
                    var newOption = document.createElement("option");
                    newOption.value = data.categoryid;
                    newOption.text = data.name;
                    selectElement.appendChild(newOption);

                    // Clear the input field and hide the category form
                    document.querySelector("#name").value = "";
                    document.getElementById("category-form").style.display = "none";
                }

                // Log to the console to verify that the event listener is working
                console.log("Form submitted via AJAX");
            })
            .catch(error => {
                // Handle errors
                console.error('Error:', error);
            });
    }

    // Add an event listener to the "savecategory" form submission
    document.querySelector("#category-form").addEventListener("submit", submitCategoryForm);
});