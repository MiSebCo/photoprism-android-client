# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed
- Updated video player library. If you have playback problems, 
consider checking out this version

### Added
- German translation

## [1.22.0] - 2023-12-15

### Added
- Slideshow, which can be launched from the media viewer
- Space between pages in the media viewer for better look
- Spanish translation

### Changed
- Made connection error message more clear when a misconfigured proxy in front of the library
prevents access to the API

### Fixed
- Not clickable "Back" toolbar button in the media viewer 
- Made the media viewer toolbar menu accessible on TV
- Squashed icons of extra media viewer menu items on Android 5

## [1.21.0] - 2023-11-08

### Added
- Ability to like/dislike content 
- Ability to see only favorite content on the search screen 

### Changed
- "Open in" and "Open in web viewer" media viewer actions are now located in the header menu

### Fixed
- Not loading gallery content if there is a file without type (`FileType`)

## [1.20.3] - 2023-10-27

### Fixed
- Not playing some HEVC videos (instant stop)

## [1.20.2] - 2023-10-25

### Fixed
- Refusing to play non-AVC videos with resolution exceeding reported device capabilities.
In fact, such videos can be played well
- Some rare content selection bugs
- The "Cancel download" button in the media viewer getting stuck after clicking it
- Crash when spamming the "Download" button in the media viewer
- Not updating dates on the gallery scroll bar when refreshing the content
- Occasional weird behavior of photo zoom

### Changed
- Media cache is now cleared when disconnecting from the library

## [1.20.1] - 2023-10-14

### Fixed
- Refusing to play HEVC video of custom size (odd width or height)

## [1.20.0] - 2023-10-12

### Fixed
- Auto screen turn-off when playing video
- Selection reset on the people overview screen when changing the orientation

### Added
- Support of Apple live photos uploaded as HEIC+MOV bundle
- Ability to download gallery items selected with a long press
- Exit the selection mode when unselecting the last selected gallery item
- Dynamic selection of the video preview format (AVC, HEVC, VP8, VP9, AV1). 
For example, if the video is in HEVC and your device supports HEVC, 
it will be played as HEVC instead of transcoding to AVC
- Traditional Chinese translation

## [1.19.0] - 2023-09-24

### Added
- Ability to disable showing folders in the album list
- Support of Google motion photos in the enhanced live photo viewer

### Changed
- Improved visibility of selected items

## [1.18.0] - 2023-09-13

### Changed
- Albums and folders are now ordered alphabetically, keeping the favorites at the beginning
- People and faces are now ordered by photo count, keeping the favorites at the beginning
- Made image placeholders darker in dark theme

### Added
- People overview screen for the search, where you can see all the people and faces in grid view, 
quickly scroll from top to bottom and filter the items by name
- Pull to refresh gesture on the main screen

## [1.17.0] - 2023-08-28

### Added
- Albums overview screen for the search, where you can see all the albums in grid view, 
quickly scroll from top to bottom and filter the items by name
- Enhanced live photos viewer which fades the video into the high-quality still image.
It works with
[Samsung live photos](https://github.com/photoprism/photoprism/issues/439#issuecomment-1687885285) 
and manually uploaded
[JPEG+MP4 batches](https://github.com/Radiokot/photoprism-android-client/issues/77#issuecomment-1682631862)
and does not affect short videos treated by PhotoPrism as live photos. 
Until the autumn PhotoPrism release,  you can test this feature on the
[public demo](https://demo.photoprism.app/library/browse)
- Ability to change the app language from system settings on Android 13

### Changed
- Added more vertical space to the search configuration screen
- Make long album names scroll like a ticker

## [1.16.1] - 2023-08-11

### Added
- A notice suggesting to use the mouse to search the library,
which is shown in the search bar when the app is running on a TV
- Distinctive `User-Agent` to be shown in the PhotoPrism sessions list
- Going full screen automatically when viewing a video
- Closing media viewer by swiping up or down
- Automatic update of album and people lists when re-opening the app after it has been moved 
to the background by pressing the "Back" navigation button.

### Changed
- Improved quality of the viewed photos. Now the app loads corresponding previews 
according to the screen size. This, however, may increase network load
- Split the welcome screen layout into 2 columns on wide landscape screens
- Limited the max width of the search bar
- The "X" button now exits the selection mode instead of just clearing, 
if the selection is initiated by a long press

### Fixed
- Crash when clicking the "Apply" button on the search screen twice
- Media viewer failing to load very big images
- Missing player controls when viewing video in rare cases

## [1.16.0] - 2023-07-31

### Added
- Search by people and new faces
- Showing full album name as a tooltip when making a long press on it
- Ability to disable loading and showing people and albums on the search configuration screen
- Ability to pick RAW photos when the gallery is opened to pick an image

### Changed
- Horizontal lists on the search configuration screen are now scrolled from edge to edge
- Selected elements on the search configuration screen now have more contrast
- Focused gallery item, when using keyboard navigation, is now better highlighted
- PhotoPrism search filter guide on the search configuration screen 
is replaced with the new gallery search guide

### Fixed
- The "Back" toolbar button in the web viewer closing it rather than navigating back

## [1.15.1] - 2023-07-24

## Fixed
- Connection screen allowing connection to a private library without credentials
- Not finding a private entry when opening in the web viewer
- Incorrect color of the "Close" web viewer button in dark theme
- Missing focus indication in file selection dialog
- Unwanted focus on the "Back" toolbar button in the media viewer
- Not intuitive behavior of the "Back" button when selecting multiple items

## [1.15.0] - 2023-07-18

### Added
- Ability to select multiple items and share them directly from the gallery.
To start selecting, make a long press on an item.

### Fixed
- Not showing the item title and date in the media viewer when selecting content

### Changed
- Use the advanced system dialog (with chats, nearby share, etc.) when sharing a file

## [1.14.0] - 2023-07-11

### Fixed
- Showing some gallery entries under the wrong day occasionally.<br>
Now, if a photo is taken on July 4th at 23:18 in Montenegro, 
its time is displayed and treated as July 4th, 23:18 
regardless of whether the gallery is viewed from Montenegro, USA or the Moon.
- Inability to connect to a library running on a slow server due to the timeout error

### Added
- Display of the item title and date in the media viewer

## [1.13.0] - 2023-07-06

### Added
- A welcome screen clarifying that this is not an official PhotoPrism client
- Ability to see the user guide in the preferences
- Ability to see the list of used open-source software in the preferences

### Fixed
- Rare crash when the app starts while not being visible. Did you know this is possible?
- Keeping a corrupted file in Downloads if the download resulted in an HTTP error
- Not previewing or downloading files after drop of all sessions on the server
- Failing download of a file already existing in Downloads but without r/w permission
- Web viewer back button not responding when browsing GitHub
- Creating multiple sessions during the renewal

### Changed
- Relicensed the source code over the compatible GPLv3 license 
- Replaced the root URL guide with the complete connection guide
- App version info in the preferences now additionally shows the app name and the build year.
Furthermore, it mentions the author and contributors and the source code is opened if you click it

## [1.12.0] - 2023-06-25

### Added
- Splash screen for old Android versions
- Disconnect suggestion when the gallery credentials have been changed

### Fixed
- Occasional blinking of gallery items while loading a new page
- Occasional incorrect width of the connection screen fields
- Not visible albums on the search configuration screen after screen rotation

### Changed
- PhotoPrism web viewer is now opened internally and has auto-login

## [1.11.0] - 2023-06-18

### Added
- Display of completed download icon in the viewer
- Display of the app in the Android TV launcher
- Ability to browse the gallery with a keyboard (or remote control).
The search remains inaccessible without a mouse for now

### Changed
- Now the file for sharing or opening is not being re-downloaded 
if it already exists in the external downloads directory
- Size of gallery items and albums is adapted for large screens
- Environment connection screen is adapted for large screens

## [1.10.0] - 2023-06-11

### Fixed
- Inability to refresh the gallery when re-opening the app after it has been moved to the background 
by pressing the "Back" navigation button. The data is now updated automatically in this case
- Incorrect orientation of some downloaded JPEGs

### Changed
- Made downloads initiated from the viewer run in background. 
Click the "Download" button and continue swiping, no more need to wait for completion

## [1.9.0] - 2023-06-03

### Added
- Monochrome icon for Android 13 and higher
- The gallery now scrolls as you swipe content in the viewer. 
Once you returned, the last viewed media is visible in the list
- Turkish, Greek, Italian, Chinese, Russian and Ukrainian translations

### Changed
- Press of the "Back" navigation button resets the chronological scroll if it is scrolled somewhere

### Fixed
- Inconsistent paddings in the dialogs 

## [1.8.1] - 2023-05-28

### Fixed
- A bookmark overcoming allowed media types when selecting content
- When scrolling to a specific month, its last day content is not shown sometimes
- Weird scroll changes when changing the gallery content (apparently)

### Added
- Ability to save the bookmarks export file to the storage

### Changed
- Speed up pages loading

## [1.8.0] - 2023-05-25

### Added
- Ability to select multiple items in the selection mode, e.g. when attaching files to Gmail

### Changed
- Press of the "Back" navigation button resets the search if it is applied
- The keyboard is no more shown automatically when opening the search screen

## [1.7.1] - 2023-05-14

### Fixed
- Laggy chronological scroll dragging
- Not bringing the list to the top when scrolling to the current month

### Added
- Display of album name in the search bar when an album is selected

## [1.7.0] - 2023-05-09

### Added
- Ability to filter library content by album (or folder)

### Changed
- Made the search bar be always visible
- Placed Apply and Reset search buttons above the keyboard

## [1.6.0] - 2023-05-02

### Fixed
- Hanging of network calls after multiple media download cancellations
- Keeping a corrupted file in the device gallery when the downloading is cancelled

### Added
- Ability to open a media in the web viewer, to see EXIF data, location, etc.
- Ability to view a media while selecting

### Changed
- Library root URL is now shown in the preferences instead of the API endpoint

## [1.5.2] - 2023-04-27

### Changed
- Made the fast scroll responsible – the content will be loaded as you are dragging

## [1.5.2-rc1] - 2023-04-26

### Fixed
- Duplication of gallery items, which happened to be at the edge of the data page
- Blinking of the empty view when the data is being loaded for the first time

### Added
- Infinite scrolling (swiping?) in the media viewer
- Clearing of the app internal downloads directory on startup, which frees "Data" storage

## [1.5.1] - 2023-04-22

### Fixed
- Non-functional video viewer when using mTLS authentication
- Displaying unmasked HTTP basic authentication credentials on the preferences screen

### Added
- Video preview caching – big videos and live photos are not be re-downloaded on each repeat

### Changed
- Lowered minimal video buffer duration requirements for faster start of playback
- Slightly improved look of video player controls

## [1.5.0] - 2023-04-20

### Added
- Video preview – now you can preview videos, animations and live photos without downloading them
- Empty view – when no media found, a corresponding view is shown instead of a blank screen

## [1.4.0] - 2023-04-12

### Added
- Preferences screen
- Bookmarks backup – export your bookmarks to restore them later or use on other device
- Disconnect from the library

## [1.3.0] - 2023-04-07

### Added
- Ability to include private content on the search screen
- Preview of RAW and vector images
- Support of mTLS (mutual TLS) authentication for Android 6 and higher
- Support of HTTP basic authentication with URL credentials (https://username:password@host.com)
- Showing an error when connecting to a private library without credentials

### Fixed
- Crash when there were too many months on the fast scroll bar

## [1.2.0] - 2023-04-03

### Added
- Month headers for the gallery items
- Chronological fast scroll – slide the scroll bar to quickly jump to a specific month
- Showing that the app is in the content selection mode

### Changed
- Made the viewer immersive – now it is dark and all the controls can be hidden by a tap
- Rearranged the action buttons on the viewer
- Made the status bar light

## [1.1.0] - 2023-03-28

### Added
- Search bookmarks – the search configuration can be bookmarked and applied later with a click
- Search filters guide shortcut to the search screen

### Changed
- "Clear text" button icon on the search screen made different from the "Reset search" button
icon on the main screen

## [1.0.2] - 2023-03-26

### Fixed
- Non-clickable files in the file selection dialog
- Search bar menu animation bug, when all the actions are visible during the animation

## [1.0.1] - 2023-03-25

### Changed
- Trust installed user CA so it is possible to use HTTPS with self-signed certificates
- Make env connection and content loading errors show the short summary of the cause

### Fixed
- "Library is not accessible" during the connection, when the entered root URL doesn't match the 
`PHOTOPRISM_SITE_URL` server config value 

[Unreleased]: https://github.com/Radiokot/photoprism-android-client/compare/1.22.0(35)...HEAD
[1.22.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.21.0(34)...1.22.0(35)
[1.21.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.20.3(33)...1.21.0(34)
[1.20.3]: https://github.com/Radiokot/photoprism-android-client/compare/1.20.2(32)...1.20.3(33)
[1.20.2]: https://github.com/Radiokot/photoprism-android-client/compare/1.20.1(31)...1.20.2(32)
[1.20.1]: https://github.com/Radiokot/photoprism-android-client/compare/1.20.0(30)...1.20.1(31)
[1.20.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.19.0(29)...1.20.0(30)
[1.19.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.18.0(28)...1.19.0(29)
[1.18.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.17.0(27)...1.18.0(28)
[1.17.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.16.1(26)...1.17.0(27)
[1.16.1]: https://github.com/Radiokot/photoprism-android-client/compare/1.16.0(25)...1.16.1(26)
[1.16.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.15.1(24)...1.16.0(25)
[1.15.1]: https://github.com/Radiokot/photoprism-android-client/compare/1.15.0(23)...1.15.1(24)
[1.15.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.14.0(22)...1.15.0(23)
[1.14.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.13.0(21)...1.14.0(22)
[1.13.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.12.0(20)...1.13.0(21)
[1.12.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.11.0(19)...1.12.0(20)
[1.11.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.10.0(18)...1.11.0(19)
[1.10.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.9.0(17)...1.10.0(18)
[1.9.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.8.1(16)...1.9.0(17)
[1.8.1]: https://github.com/Radiokot/photoprism-android-client/compare/1.8.0(15)...1.8.1(16)
[1.8.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.7.1(14)...1.8.0(15)
[1.7.1]: https://github.com/Radiokot/photoprism-android-client/compare/1.7.0(13)...1.7.1(14)
[1.7.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.6.0(12)...1.7.0(13)
[1.6.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.5.2(11)...1.6.0(12)
[1.5.2]: https://github.com/Radiokot/photoprism-android-client/compare/1.5.2-rc1(10)...1.5.2(11)
[1.5.2-rc1]: https://github.com/Radiokot/photoprism-android-client/compare/1.5.1(9)...1.5.2-rc1(10)
[1.5.1]: https://github.com/Radiokot/photoprism-android-client/compare/1.5.0(8)...1.5.1(9)
[1.5.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.4.0(7)...1.5.0(8)
[1.4.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.3.0(6)...1.4.0(7)
[1.3.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.2.0(5)...1.3.0(6)
[1.2.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.1.0(4)...1.2.0(5)
[1.1.0]: https://github.com/Radiokot/photoprism-android-client/compare/1.0.2(3)...1.1.0(4)
[1.0.2]: https://github.com/Radiokot/photoprism-android-client/compare/1.0.1(2)...1.0.2(3)
[1.0.1]: https://github.com/Radiokot/photoprism-android-client/compare/1.0.0(1)...1.0.1(2)
