async function shortenLink(url, outputElementId) 
{
    var shownLinkElement = document.getElementById(outputElementId);
    var selftaddress = window.location.origin; // Get the current page URL
    shownLinkElement.classList.remove("redtext");

    if (!isValidUrl(url))
    {
        shownLinkElement.textContent = "Invalid URL. Please enter a valid URL.";
        shownLinkElement.classList.add("redtext");
        return;
    }

    const geturlshortened = await fetch('/dba/addlink', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({url: url})
    })
    .then(response => response.text())
    .then(data => {
        return data; // Assuming the server returns a JSON object with the shortened URL
    });

    shownLinkElement.textContent = selftaddress + "/" + geturlshortened;

}

function isValidUrl(url)
{
    // Simple URL validation (you can enhance this as needed)
    const urlPattern = /^(https?:\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/;
    return urlPattern.test(url);
}
