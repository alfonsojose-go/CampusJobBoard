function handleDeleteClick(event, title) {
    if (!confirm('Do you want to delete "' + title + '"?')) {
        event.preventDefault();
        event.stopPropagation();
        return false;
    }
    return true;
}