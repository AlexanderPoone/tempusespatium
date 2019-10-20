//
//  TopicSearcherViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 6/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import DropDown
import Alamofire
import Ono
import SearchTextField
import TagListView
import AVFoundation
import FlagKit

//extension DataRequest {
//    static func xmlResponseSerializer() -> DataResponseSerializer<ONOXMLDocument> {
//        return DataResponseSerializer { request, response, data, error in
//            // Pass through any underlying URLSession error to the .network case.
//            guard error == nil else { return .failure(BackendError.network(error: error!)) }
//
//            // Use Alamofire's existing data serializer to extract the data, passing the error as nil, as it has
//            // already been handled.
//            let result = Request.serializeResponseData(response: response, data: data, error: nil)
//
//            guard case let .success(validData) = result else {
//                return .failure(BackendError.dataSerialization(error: result.error! as! AFError))
//            }
//
//            do {
//                let xml = try ONOXMLDocument(data: validData)
//                return .success(xml)
//            } catch {
//                return .failure(BackendError.xmlSerialization(error: error))
//            }
//        }
//    }
//
//    @discardableResult
//    func responseXMLDocument(
//        queue: DispatchQueue? = nil,
//        completionHandler: @escaping (DataResponse<ONOXMLDocument>) -> Void)
//        -> Self
//    {
//        return response(
//            queue: queue,
//            responseSerializer: DataRequest.xmlResponseSerializer(),
//            completionHandler: completionHandler
//        )
//    }
//}

class GradientView: UIView {
    override open class var layerClass: AnyClass {
        return CAGradientLayer.classForCoder()
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        let gradientLayer = layer as! CAGradientLayer
        gradientLayer.colors = [UIColor(named: "UbuntuOrange")!, UIColor(named: "CanonicalAubergine")!]
    }
}

extension String {
    mutating func removingRegexMatches(_ pattern: String) {
        do {
            let regex = try NSRegularExpression(pattern: pattern, options: NSRegularExpression.Options.caseInsensitive)
            self = regex.stringByReplacingMatches(in: self, options: [], range: NSMakeRange(0, self.count), withTemplate: "")
        } catch {
            return
        }
    }
}

class TopicSearcherViewController: UIViewController, TagListViewDelegate {
    
    var beep, swoosh: AVAudioPlayer?
    
    @IBOutlet weak var mResetBtn: UIButton!
    
    @IBAction func mResetClicked() {
        beep!.play()
        mTopicAutocomplete.text = ""
        view.endEditing(true)
        mTopicAutocomplete.becomeFirstResponder()
    }
    
    @IBOutlet weak var mSubmitAndPlayBtn: UIButton!
    
    @IBAction func mSubmitAndPlayClicked() {
        if mSubmitAndPlayBtn.state != .disabled {
            swoosh!.play()
            performSegue(withIdentifier: "launchRound1", sender: nil)
        }
    }
    
    
    @IBOutlet weak var mLocaleLbl: UILabel!
    
    @IBOutlet weak var mAnyTopicLbl: UILabel!
    
    
    @IBOutlet weak var mTopicAutocomplete: SearchTextField!
    
    
    @IBOutlet weak var mSelectAnyHeader: UILabel!
    
    @IBOutlet weak var mArticlesCoveredLbl: UILabel!
    
    @IBOutlet weak var mTagsView: TagListView!
    @IBOutlet weak var mClickOnBadgeLbl: UILabel!
    
    @IBOutlet weak var mLocaleCurrentSelection: LocaleCurrentSelectionWithArrowView!
    
    @IBOutlet weak var mReturnBtn: UIButton!
    
    
    private var mLocaleDropDown:DropDown?
    
    private var mTopics:[String:String] = [:]
    
    private var mQuestionLang:String?
    
    func english() {
        mTopicAutocomplete.filterStrings([])
        AF.request("https://en.wikipedia.org/wiki/Wikipedia:Lists_of_popular_pages_by_WikiProject")
            .validate(statusCode: 200..<300)
            //            .validate(contentType: ["application/json"])
            .responseData { response in
                switch response.result {
                case .success(let value):
                    print("asdfsuccess")
                    //                    guard let url = Bundle.main.url(forResource: "nutrition", withExtension: "html"),
                    //                        let data = try? Data(contentsOf: url) else
                    //                    {
                    //                        fatalError("Missing resource: nutrition.xml")
                    //                    }
                    //
                    do {
                        let document = try ONOXMLDocument(data: response.data)
                        document.enumerateElements(withXPath: "//*[@id=\"mw-content-text\"]/div/table/tbody/tr/td[1]/a") { (element, _, _) in
                            
                            var ele:String = element.stringValue!
                            print(ele)
                            ele.removingRegexMatches("Wikipedia:(WikiProject )?")
                            ele.removingRegexMatches("/(Popular|Most-viewed|Favourite) pages")
                            ele.removingRegexMatches("/(Popular|Article hits)")
                            ele.removingRegexMatches("( task force| work group)")
                            ele.removingRegexMatches("Taskforces/(BPH/)?")
                            self.mTopics[ele.replacingOccurrences(of: "/", with: " > ")] = (element.value(forAttribute: "href") as! String)
                            
                        }
                        self.mTopicAutocomplete.startSuggestingImmediately = true
                        self.mTopicAutocomplete.startVisible = true
                        self.mTopicAutocomplete.startVisibleWithoutInteraction = true
                        self.mTopicAutocomplete.itemSelectionHandler = { filteredResults, itemPosition in
                            let item = filteredResults[itemPosition]
                            self.mTopicAutocomplete.text = item.title
                            
                            AF.request("https://en.wikipedia.org\(self.mTopics[item.title]!)")
                                .validate(statusCode: 200..<300)
                                //            .validate(contentType: ["application/json"])
                                .responseData { response in
                                    switch response.result {
                                    case .success(let value):
                                        print("fdsasucc")
                                        var articlesIncluded:[String] = []
                                        do {
                                        let document = try ONOXMLDocument(data: response.data)
                                        document.enumerateElements(withXPath: "//*[@id=\"mw-content-text\"]/div/table/tbody/tr/td[2]/a") { (element, _, _) in
                                            print(element.stringValue!)
                                            articlesIncluded.append(element.stringValue!)
                                            }
                                        } catch {
                                            print("Oh no!")
                                        }
//                                        self.mTagsView.removeAllTags()
//                                        self.mTagsView.addTags(articlesIncluded)
                                    case .failure(let error):
                                        print("rewqerror")
                                        print(error)
                                    }
                            }
                        }
                    } catch {
                        print("Oh no!")
                    }
                    
                    self.mTopicAutocomplete.filterStrings(Array(self.mTopics.keys))
                    
                    //                    let document = try ONOXMLDocument(data: response.data) {
                    //
                    //                    //document.rootElement.tag
                    //
                    //                    for element in document.rootElement.children.first?.children ?? [] {
                    //                        let nutrient = element.tag
                    //                        let amount = element.numberValue!
                    //                        let unit = element.attributes["units"]!
                    //
                    //                        print("- \(amount)\(unit) \(nutrient)")
                    //                    }
                    
                    //                    document.enumerateElements(withXPath: "//food/name") { (element, _, _) in
                    //                        print(element)
                //                    }
                case .failure(let error):
                    print("asdferror")
                    print(error)
                }
        }
    }
    
    func catala() {
        
    }
    
    func francais() {
        
    }
    
    func espanol() {
        
    }
    
    func deutsch() {
        
    }
    
    func ukraiynska() {
        
    }
    
    @objc func mShowDropDown(tapGestureRecognizer:UITapGestureRecognizer) {
        mLocaleDropDown!.show()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        beep = setupAudioPlayer(withFile: "beep_space_button", type: "wav")
        swoosh = setupAudioPlayer(withFile: "space_swoosh", type: "wav")
        
        mResetBtn!.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.undo), iconColor: .white, postfixText: NSLocalizedString("reset", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "danger")!, forState: .normal, textSize: nil, iconSize: nil)
        mSubmitAndPlayBtn!.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.gamepad), iconColor: .white, postfixText: NSLocalizedString("submit_and_play", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "info")!, forState: .normal, textSize: nil, iconSize: nil)
        
        mReturnBtn!.setIcon(prefixText: "", prefixTextColor: .white, icon: .fontAwesomeSolid(.signOutAlt), iconColor: .white, postfixText: NSLocalizedString("return_main_menu", comment: ""), postfixTextColor: .white, backgroundColor: UIColor(named: "danger")!, forState: .normal, textSize: nil, iconSize: nil)
        
        mSelectAnyHeader.text = NSLocalizedString("topic_searcher_heading", comment: "")
        
        mTopicAutocomplete.placeholder = NSLocalizedString("enter_keyword", comment: "")
        
        mTopicAutocomplete.filterStrings(["Cymru", "Alba", "Eire", "England"])
        
        mTagsView.addTags(["Cardiff", "Glasgow", "Cork", "Canterbury"])
        mTagsView.textFont = UIFont.systemFont(ofSize: 24)
        mTagsView.cornerRadius = 15
        mTagsView.delegate = self
        
        mLocaleDropDown = DropDown()
        
        mLocaleDropDown!.anchorView = mLocaleCurrentSelection
        
        let langs:[String:[String]] = ["English": ["English", "GB"], "català": ["Catalan", "AD"], "français": ["French","FR"], "Deutsch": ["German", "DE"], "español": ["Spanish","ES"], "Українська": ["Ukrainian","UA"]]
        
        
        mLocaleDropDown!.dataSource = Array(langs.keys)
        
        mLocaleDropDown!.cellNib = UINib(nibName: "LocaleDropDownTableViewCell", bundle: nil)
        
        
        mLocaleDropDown!.customCellConfiguration = { (index: Index, item: String, cell: DropDownCell) -> Void in
            guard let cell = cell as? LocaleDropDownTableViewCell else { return }
            cell.mSubtitle.text = langs[item]![0]
            let flag = Flag(countryCode: langs[item]![1])!
            cell.mFlag.image = flag.originalImage
        }
        
        mLocaleDropDown!.selectionAction = { [unowned self] (index: Int, item: String) in
            self.mLocaleCurrentSelection.mSelection.text = item
            self.mLocaleCurrentSelection.mSubtitle.text = langs[item]![0]
            let iso = langs[item]![1]
            let flag = Flag(countryCode: iso)!
            self.mLocaleCurrentSelection.mFlag.image = flag.originalImage
            
            switch iso {
            case "GB":
                self.english()
            case "AD":
                self.catala()
            case "DE":
                self.deutsch()
            case "ES":
                self.espanol()
            case "FR":
                self.francais()
            case "UA":
                self.ukraiynska()
            default:
                break
            }
            self.mQuestionLang = iso
        }
        
        
        mLocaleDropDown!.textColor = .white
        
        let singleTap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(mShowDropDown))
        singleTap.numberOfTapsRequired = 1
        singleTap.cancelsTouchesInView = false
        mLocaleCurrentSelection.addGestureRecognizer(singleTap)
        
//        mLocaleDropDown!.show()
        
        english()
        mQuestionLang = "GB"
    }
    
    
    func setupAudioPlayer(withFile file: String, type: String) -> AVAudioPlayer? {
        let path = Bundle.main.path(forResource: file, ofType: type)
        let url = NSURL.fileURL(withPath: path!)
        return try? AVAudioPlayer(contentsOf: url)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        switch segue.identifier {
        case "launchRound1":
            let destinationVC = segue.destination as! Round1ViewController
        case "popUpLearn":
            let destinationVC = segue.destination as! LearnViewController
            destinationVC.mUrl = (sender as! String)
        default:
            break
        }
    }
    
    func tagPressed(_ title: String, tagView: TagView, sender: TagListView) {
        let url = "https://en.wikipedia.org/wiki/\(title.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!)"
        performSegue(withIdentifier: "popUpLearn", sender: url)
    }
    
}
