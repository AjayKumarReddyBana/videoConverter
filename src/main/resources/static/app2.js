document.addEventListener("DOMContentLoaded", () => {
    const albumList = document.getElementById("albumList");
    const addAlbumForm = document.getElementById("addAlbumForm");

    // Function to fetch and display existing albums
    const fetchAlbums = () => {
        fetch("http://localhost:8080/api/demo/getallcds") // Ensure the URL is correct
            .then(response => response.json())
            .then(albums => {
                albumList.innerHTML = ""; // Clear the existing list
                if (albums.length === 0) {
                    albumList.innerHTML = "<p>No albums found.</p>";
                } else {
                    albums.forEach(album => {
                        const albumElement = document.createElement("div");
                        albumElement.classList.add("mb-3", "card");
                        
                        // Split the songs string into an array using ',' as a delimiter
                        const songList = album.songs.split(",").map(song => song.trim());

                        albumElement.innerHTML = `
                            <div class="card-body">
                                <h5 class="card-title">${album.albumName}</h5>
                                <p class="card-text">Artist: ${album.artistName}</p>
                                <p class="card-text">Songs:</p>
                                <ul>
                                    ${songList.map(song => `<li>${song}</li>`).join("")}
                                </ul>
                            </div>
                        `;
                        albumList.appendChild(albumElement);
                    });
                }
            })
            .catch(error => console.error("Error fetching albums:", error));
    };

    // Call fetchAlbums on page load
    fetchAlbums();

    // Function to handle form submission and add a new album
    addAlbumForm.addEventListener("submit", (event) => {
        event.preventDefault(); // Prevent page reload

        const artistName = document.getElementById("artistName").value;
        const albumName = document.getElementById("albumName").value;
        const songs = document.getElementById("songs").value; // No need to split songs here

        const newAlbum = {
            artistName: artistName,
            albumName: albumName,
            songs: songs // Keep as a single string
        };

        fetch("http://localhost:8080/api/demo/add/cd", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(newAlbum)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to add album");
            }
            return response.json();
        })
        .then(data => {
            console.log("Album added:", data);
            // Clear the form fields
            document.getElementById("artistName").value = "";
            document.getElementById("albumName").value = "";
            document.getElementById("songs").value = "";
            // Fetch and display the updated list of albums
            fetchAlbums();
            
            // Close the modal after submission
            const modal = bootstrap.Modal.getInstance(document.getElementById('addAlbumModal')); 
            modal.hide();
        })
        .catch(error => console.error("Error adding album:", error));
    });
});
