//
//  ExteriorViewController.swift
//  Tempus E Spatium
//
//  Created by Victor Poon on 18/10/2019.
//  Copyright © 2019 SoftFeta. All rights reserved.
//

import UIKit
import SwiftIcons
import YLProgressBar
import DACircularProgress
import SVGKit
import Alamofire
import Ono

class ExteriorViewController: UIViewController { //, UIPickerViewDataSource, UIPickerViewDelegate
    
//    func numberOfComponents(in pickerView: UIPickerView) -> Int {
//        return 1
//    }
//
//    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
//        return (1912...1949).count
//    }
//
//    func pickerView(_ pickerView: UIPickerView, attributedTitleForRow row: Int, forComponent component: Int) -> NSAttributedString? {
//
//        let string = String(Array(1912...1949)[row])
//        return NSAttributedString(string: string, attributes: [NSAttributedString.Key.foregroundColor: UIColor.white])
//    }
    
    @IBOutlet weak var mPauseBtn: UIButton!
    
    @IBOutlet weak var mDonutTime: DALabeledCircularProgressView!
    
    @IBOutlet weak var mFragmentContainer: UIView!
    
    @IBOutlet weak var mScoreBar: YLProgressBar!
    
    @IBOutlet weak var mScoreText: UILabel!
    
    @IBOutlet weak var mScoreChangeLbl: UILabel!
    
    @IBOutlet weak var mYellowBtn: UIButton!
    
    @IBOutlet weak var mCooldownTxt: UILabel!
    
    private let mPreferences = UserDefaults.standard
    
//    @objc func aClicked() {
//        print("A Clicked")
//        if let view1 = mFragmentContainer.subviews.first {
//            showLoadingDialog()
//            view1.removeFromSuperview()
//            let controller = storyboard!.instantiateViewController(withIdentifier: "MapGameViewController") as! MapGameViewController
//            controller.view.frame = mFragmentContainer.bounds
//            controller.willMove(toParent: self)
//            mFragmentContainer.addSubview(controller.view)
//            addChild(controller)
//            closeLoadingDialog()
//            controller.didMove(toParent: self)
//            controller.view.backgroundColor = view.backgroundColor
//        }
//    }
//
//    let mMonthDelegate = MonthPickerDelegate() //Must be a member, since dataSource and delegate are weak
//
//    @objc func incorrect() {
//        dController!.mYearScroller.backgroundColor = UIColor(named: "Firebrick")!
//        dController!.mMonthScroller.backgroundColor = UIColor(named: "IndianRed")!
//        dController!.mYearScroller.isUserInteractionEnabled = false
//        dController!.mMonthScroller.isUserInteractionEnabled = false
//    }
//
//    @objc func bClicked() {
//        print("B Clicked")
//        if let view1 = mFragmentContainer.subviews.first {
//            showLoadingDialog()
//            view1.removeFromSuperview()
//            dController = storyboard!.instantiateViewController(withIdentifier: "DateGameViewController") as! DateGameViewController
//            dController!.view.frame = mFragmentContainer.bounds
//            dController!.willMove(toParent: self)
//            mFragmentContainer.addSubview(dController!.view)
//            addChild(dController!)
//            closeLoadingDialog()
//            dController!.didMove(toParent: self)
//            dController!.view.backgroundColor = view.backgroundColor
//
//            //            controller.mYear = 1937
//            //            controller.mMonth = 7
//            //
//            dController!.mYearScroller.dataSource = self
//            dController!.mYearScroller.delegate = self
//            dController!.mYearScroller.selectRow((0..<dController!.mYearScroller.numberOfRows(inComponent: 0)).randomElement()!, inComponent: 0, animated: false)
//
//            dController!.mMonthScroller.dataSource = mMonthDelegate
//            dController!.mMonthScroller.delegate = mMonthDelegate
//            dController!.mMonthScroller.selectRow((0..<dController!.mMonthScroller.numberOfRows(inComponent: 0)).randomElement()!, inComponent: 0, animated: false)
//            dController!.mSubmitBtn.addTarget(self, action: #selector(incorrect), for: .touchDown)
//
//            let url = URL(string: "https://upload.wikimedia.org/wikipedia/commons/8/85/Tank_Battle_in_Golan_Heights_-_Flickr_-_The_Central_Intelligence_Agency.jpg")
//            let data = try! Data(contentsOf: url!)
//            dController!.mHintPic.image = UIImage(data: data)
//        }
//    }
//
//    @objc func cClicked() {
//        print("C Clicked")
//        parent!.performSegue(withIdentifier: "toHighscoreInputDialog", sender: nil)
//    }
//
//    @objc func dClicked() {
//        print("D Clicked")
//        reveal()
//    }
//
//    func reveal() {
//        if let ctrl = fController {
//            mScoreText.text = String(format: NSLocalizedString("bar_points", comment: ""), 12)
//            ctrl.mIndicatorA.setIcon(icon: .googleMaterialDesign(.newReleases), iconSize: 24, color: .white, bgColor: UIColor(named: "Amber")!)
//            ctrl.mClickAreaA.backgroundColor = UIColor(named: "Amber")!
//        }
//    }
//
//    var fController:FlagGameViewController?
//    var dController:DateGameViewController?
//
//    func showLoadingDialog() {
//        let alert = UIAlertController(title: nil, message: NSLocalizedString("loading", comment: ""), preferredStyle: .alert)
//
//        let loadingIndicator = UIActivityIndicatorView(frame: CGRect(x: 10, y: 5, width: 50, height: 50))
//        loadingIndicator.hidesWhenStopped = true
//        loadingIndicator.style = .medium
//        loadingIndicator.startAnimating()
//
//        alert.view.addSubview(loadingIndicator)
//        present(alert, animated: false)
//    }
//
//    func closeLoadingDialog() {
//        dismiss(animated: false)
//    }
//
//    @IBAction func mYellowTest(_ sender: Any) {
//        if let view1 = mFragmentContainer.subviews.first {
//            view1.removeFromSuperview()
//
//            fController = storyboard!.instantiateViewController(withIdentifier: "FlagGameViewController") as! FlagGameViewController
//            fController!.view.frame = mFragmentContainer.bounds
//            fController!.willMove(toParent: self)
//            mFragmentContainer.addSubview(fController!.view)
//            addChild(fController!)
//            fController!.didMove(toParent: self)
//            fController!.view.backgroundColor = view.backgroundColor
//
//            //AlamoFire here!
//            AF.request("https://en.wikipedia.org/wiki/Wikipedia:Lists_of_popular_pages_by_WikiProject")
//                .validate(statusCode: 200..<300)
//                //            .validate(contentType: ["application/json"])
//                .responseData { response in
//                    switch response.result {
//                    case .success(let value):
//                        print("asdfsuccess")
//                    case .failure(let error):
//                        print(error)
//                    }
//            }
//
//            let population = 31908551587
//            let populationNumberFormatter = NumberFormatter()
//            populationNumberFormatter.numberStyle = .decimal
//
//            fController!.mQuestion.text = "\u{1F465}: \(populationNumberFormatter.string(from: NSNumber(value: population))!) (2019)"
//
//            let aClickedList = UITapGestureRecognizer(target: self, action: #selector(aClicked))
//            fController!.mClickAreaA.addGestureRecognizer(aClickedList)
//
//            let bClickedList = UITapGestureRecognizer(target: self, action: #selector(bClicked))
//            fController!.mClickAreaB.addGestureRecognizer(bClickedList)
//
//            let cClickedList = UITapGestureRecognizer(target: self, action: #selector(cClicked))
//            fController!.mClickAreaC.addGestureRecognizer(cClickedList)
//
//            let dClickedList = UITapGestureRecognizer(target: self, action: #selector(dClicked))
//            fController!.mClickAreaD.addGestureRecognizer(dClickedList)
//
//            fController!.mIndicatorB.setIcon(icon: .googleMaterialDesign(.check), iconSize: 24, color: .white, bgColor: UIColor(named: "success")!)
//            fController!.mIndicatorC.setIcon(icon: .googleMaterialDesign(.close), iconSize: 24, color: .white, bgColor: UIColor(named: "danger")!)
//
//            var svg1:SVGKImage = SVGKImage(contentsOf: URL(string: "https://upload.wikimedia.org/wikipedia/commons/d/de/Coat_of_arms_of_Botswana.svg"))
//            let fast = SVGKFastImageView(svgkImage: svg1)!
//            let aspect = fast.frame.width / fast.frame.height
//            fast.contentMode = .scaleAspectFill
//            fast.frame = CGRect(x: 0, y: 0, width: 90 * aspect, height: 90)
//            //            print(controller.mFlagA.frame)
//            fController!.mFlagA.contentMode = .scaleAspectFill
//            fController!.mFlagA.addSubview(fast)
//
//            self.mScoreBar.progress = 0.7
//            self.mScoreText.text = String(format: NSLocalizedString("bar_points", comment: ""), 70)
//
//        }
//    }
    
    @IBAction func mPauseBtnClicked(_ sender: Any) {
        performSegue(withIdentifier: "showPauseDialog", sender: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        mYellowBtn.isHidden = true
        mScoreBar.progress = 0
        mScoreBar.stripesColor = UIColor(named: self.mPreferences.string(forKey: "PREF_PLAYER_1_THEME")!)!
        mScoreText.text = String(format: NSLocalizedString("bar_points", comment: ""), 0)
        
        mPauseBtn.setIcon(icon: .fontAwesomeSolid(.pause), iconSize: nil, color: .white, backgroundColor: UIColor(named: "info")!, forState: .normal)
        mDonutTime.roundedCorners = 5
        mDonutTime.trackTintColor = UIColor(named: "BlueDialogBackground")!
        mDonutTime.progressTintColor = UIColor(named: "Liberty")!
        mDonutTime.progressLabel.textColor = UIColor(named: "MidnightBlue")!
        
//        AF.request("https://en.wikipedia.org/wiki/Queen_Mab_(poem)")
//            .validate(statusCode: 200..<300)
//            .responseData { response in
//                switch response.result {
//                case .success(let value):
//                    let blanks = self.children.first! as! BlanksGameViewController
//                    blanks.view.backgroundColor = self.view.backgroundColor
//
//                    let html1 = "<html><head></head><body style=\"background-color:\(self.view.backgroundColor!.hexString);\">"
//                    let html2 = "</body></html>"
//
//                    let document = try! ONOXMLDocument(data: response.data)
//                    document.enumerateElements(withXPath: "//*[@id=\"mw-content-text\"]/div/table/tbody/tr/td[1]/a") { (element, _, _) in
//
//                        var ele:String = element.stringValue!
//                    }
//
//                    blanks.mWebView.loadHTMLString("\(html1)<small>Test!</small>\(html2)", baseURL: nil)
//                case .failure(let error):
//                    print(error)
//                }
//        }
    }
    
}
