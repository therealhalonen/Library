var selectElement = document.getElementById("category");
var optionElement = selectElement.options[selectElement.selectedIndex];
var value = optionElement.value;
var categoryForm = document.getElementById("category-form");

if (value === "NEW_CATEGORY") {
	// Show the category form
	categoryForm.style.display = "block";
} else {
	// Keep the category form hidden
	categoryForm.style.display = "none";
}

// Function to reload the page
function reloadPage() {
	window.location.reload();
}

// Submit the category form via ajax
document.querySelector("#category-form").addEventListener("submit", function(event) {
	event.preventDefault(); // Prevent the default form submission

	var categoryData = {
		name: document.querySelector("#name").value
	};

	fetch('/savecategory', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(categoryData)
	})
		.then(response => response.json())
		.then(data => {
			// Handle the response data
			console.log(data);

			// Reload the page ONLY if the category was successfully added to database
			if (data.success) {
				reloadPage();
			}
		})
		.catch(error => {
			// Handle errors
			console.error('Error:', error);
		});
});